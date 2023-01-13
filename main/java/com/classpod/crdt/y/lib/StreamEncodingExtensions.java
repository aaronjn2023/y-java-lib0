package com.classpod.crdt.y.lib;

import com.classpod.crdt.y.lib.encoding.YjsEncoder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * encoding扩展类
 *
 * @Author jiquanwei
 * @Date 2022/9/15 3:04 PM
 **/
public class StreamEncodingExtensions {

    public static final Integer maxStrBuffer = 30000;
    public static final Integer maxStrSize = maxStrBuffer / 3;
    public ByteArrayOutputStream output;
    public StreamEncodingExtensions(){
        output = new ByteArrayOutputStream();
    }

    public static void writeUint16(ByteArrayOutputStream output, short num){
        output.write((byte)(num & Bits.Bits8));
        output.write((byte)((num >> 8) & Bits.Bits8));
    }

    public static void writeUint32(ByteArrayOutputStream output,int num){
        for(int i=0;i<4;i++){
            output.write((byte)(num & Bits.Bits8));
            num >>= 8;
        }
    }

    public static void writeVarUint(ByteArrayOutputStream output,long num){
        while(num > Bits.Bits7){
            output.write((byte)(Bit.Bit8 | (Bits.Bits7 & num)));
            num >>= 7;
        }
        output.write(Bits.Bits7 & (int)num);
    }

    public static void writeVarInt(ByteArrayOutputStream output,long num){
        boolean isNegative = num == 0 ? false : num < 0;
        if(isNegative){
            num = -num;
        }
        output.write((byte)((num > Bits.Bits6 ? Bit.Bit8 : 0) | (isNegative ? Bit.Bit7 : 0) | (Bits.Bits6 & num)));
        num >>= 6;
        while(num > 0){
            output.write((byte)((num > Bits.Bits7 ? Bit.Bit8 : 0) | (Bits.Bits7 & num)));
            num >>= 7;
        }
    }

    public static void writeVarString(ByteArrayOutputStream out,String str){
        String encodedString = ClasspodYjsEncode.unescape(str);
        int len = encodedString.length();
        writeVarUint(out,len);
        for(int i=0;i<len;i++){
            writeUint8(out,encodedString.charAt(i));
        }
//        byte[] data = str.getBytes(StandardCharsets.UTF_8);
//        writeVarUint8Array(out,data);
    }

    public static void writeVarUint8Array(ByteArrayOutputStream output,byte[] array){
        writeVarUint(output,array.length);
        output.write(array,0,array.length);
    }

    public static byte[] toUint8Array(ByteArrayOutputStream output){
        return output.toByteArray();
    }

    public static void writeUint8(ByteArrayOutputStream output,int num){
        output.write(num);
    }

    public static void writeAny(ByteArrayOutputStream output,Object o){
        if(o instanceof String){// type 119
            output.write(119);
            writeVarString(output,String.valueOf(o));
        }else if(o instanceof Boolean){// TYPE 120/121: boolean (true/false)
            output.write((byte)((Boolean) o ? 120 : 121));
        }else if(o instanceof Double){// TYPE 123: FLOAT64
            byte[] dBytes = BitConverter.getBytes((Double)o);
            if(BitConverter.isLittleEndian()){
                ArrayReverse.arrayReverse(dBytes);
            }
            output.write(123);
            output.write(dBytes,0,dBytes.length);
        }else if(o instanceof Float){// TYPE 124: FLOAT32
            byte[] fBytes = BitConverter.getBytes((Float)o);
            if(BitConverter.isLittleEndian()){
                ArrayReverse.arrayReverse(fBytes);
            }
            output.write(124);
            output.write(fBytes,0,fBytes.length);
        }else if(o instanceof Integer){// TYPE 125: INTEGER
            output.write(125);
            writeVarInt(output,(int)o);
        }else if(o instanceof Long){// Special case: treat LONG as INTEGER.
            output.write(125);
            writeVarInt(output,(long)o);
        }else if(o == null){// TYPE 126: null  TYPE 127: undefined
            output.write(126);
        }else if(o instanceof Byte[]){// TYPE 116: ArrayBuffer
            output.write(116);
            writeVarUint8Array(output,(byte[])o);
        }else if(o instanceof Map){// TYPE 118: object (Dictionary<string, object>)
            output.write(118);
            writeVarUint(output,(((Map<?, ?>) o).size()));
            for(Map.Entry<?,?> entry: ((Map<?, ?>) o).entrySet()){
                writeVarString(output,String.valueOf(entry.getKey()));
                writeAny(output,entry.getValue());
            }
        }else if(o instanceof Set){// TYPE 117
            output.write(117);
            writeVarUint(output,((Set<?>) o).size());
            for(Object obj : (Set)o){
                writeAny(output,obj);
            }
        }else{
            throw new RuntimeException("Unsupported object type.");
        }

    }

    public static void writeVarUint(YjsEncoder encoder, int num) {
        while (num > BitContanst.BITS7) {
            write(encoder, BitContanst.BIT8 | (BitContanst.BITS7 & num));
            num >>>= 7;
        }
        write(encoder, BitContanst.BITS7 & num);
    }

    public static void writeVarInt(YjsEncoder encoder,int num){
        Boolean flag = num < 0;
        if(flag){
            num = -num;
        }
        write(encoder,(num > BitContanst.BITS6 ? BitContanst.BIT8 : 0) | (flag ? BitContanst.BIT7 : 0) | (BitContanst.BITS6 & num));
        while(num > 0){
            write(encoder, (num > BitContanst.BITS7 ? BitContanst.BIT8 : 0) | (BitContanst.BITS7 & num));
            num = (int)Math.floor(num / 128);
        }
    }

    public static void write(YjsEncoder encoder, int num){
        int bufferLen = encoder.getCbuf().length;
        if (encoder.getCpos() == bufferLen) {
            encoder.getBufs().add(encoder.getCbuf());
            encoder.setCbuf(new int[bufferLen * 2]);
            encoder.setCpos(0);
        }
        encoder.getCbuf()[encoder.getCpos()] = num;
        encoder.addcpos();
    }

    public static int[] toUint8Array(YjsEncoder encoder){
        int[] uint8Arr = new int[length(encoder)];
        int[] tempArray = new int[length(encoder)];
        int curPos = 0;
        for(int i=0;i<encoder.getBufs().size();i++){
            int[] d = encoder.getBufs().get(i);
            System.arraycopy(d,0,tempArray,curPos,d.length);
            curPos += d.length;
        }
        // curr int[]
        int[] currInt = encoder.getCbuf();
        int pos = encoder.getCpos();
        uint8Arr = Arrays.copyOfRange(tempArray,0,tempArray.length);
        // 将原数组的数据copy到uint8Arr
        System.arraycopy(currInt,pos,uint8Arr,uint8Arr.length,currInt.length);
        return uint8Arr;
    }

    public static int[] createUint8ArrayViewFromArrayBuffer(int[] arr,int start,int end){
        return Arrays.copyOfRange(arr, start, end);
    }

    public static void  writeVarUint8Array(YjsEncoder encoder, int[] uint8Array){
        writeVarUint(encoder, uint8Array.length);
        writeUint8Array(encoder, uint8Array);
    }


    public static void writeUint8Array(YjsEncoder encoder, int[] uint8Array){
        int bufferLen = encoder.getCbuf().length;
        int cpos = encoder.getCpos();
        int leftCopyLen = Math.min(bufferLen - cpos, uint8Array.length);
        int rightCopyLen = uint8Array.length - leftCopyLen;
        System.arraycopy(encoder.getCbuf(),encoder.getCpos(),uint8Array,0,leftCopyLen);
        encoder.setCpos(encoder.getCpos()+leftCopyLen);
        if (rightCopyLen > 0) {
            // Still something to write, write right half..
            // Append new buffer
            Collections.addAll(encoder.getBufs(), encoder.getCbuf());
            // must have at least size of remaining buffer
            encoder.setCbuf(new int[Math.max(bufferLen * 2, rightCopyLen)]);
            // copy array
            System.arraycopy(encoder.getCbuf(),encoder.getCpos(),uint8Array,0,leftCopyLen);
            encoder.setCpos(rightCopyLen);
        }
    }

    public static void writeAny(YjsEncoder encoder,Object obj){

    }

    public static void writeVarString(YjsEncoder encoder,String str){
        // todo 判断str是否是文本类型
        writeVarStringPolyfill(encoder,str);
    }

    public static void writeVarStringNative(YjsEncoder encoder,String str){
        try {
            if(null == str || "".equalsIgnoreCase(str)){
                return;
            }
            String encoderString = URLEncoder.encode(ClasspodYjsEncode.escape(str),"UTF-8");;
            if(str.length() < maxStrSize){
                // 编码后的长度
                int count = encoderString.length();
                for(int i = 0;i<count;i++){
                    write(encoder,encoderString.charAt(i));
                }

            }else{
                int[] stringArray = new int[encoderString.length()];
                for(int i=0;i<encoderString.length();i++){
                    stringArray[i] = encoderString.charAt(i);
                }
                writeVarUint8Array(encoder,stringArray);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void writeVarStringPolyfill(YjsEncoder encoder,String str){
        try{
            String encodedString = ClasspodYjsEncode.unescape(str);
            int len = encodedString.length();
            writeVarUint(encoder,len);
            for(int i=0;i<encodedString.length();i++){
                write(encoder,encodedString.charAt(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int length(YjsEncoder encoder){
        int len = encoder.getCpos();
        for(int i = 0;i<encoder.getBufs().size();i++){
            len += encoder.getBufs().get(i).length;
        }
        return len;
    }

    public static YjsEncoder createEncoder() {
        return new YjsEncoder();
    }
}
