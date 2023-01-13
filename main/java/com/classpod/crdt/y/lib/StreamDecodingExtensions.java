package com.classpod.crdt.y.lib;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.classpod.crdt.y.lib.decoding.YjsDecoder;
import com.classpod.crdt.y.lib.dto.DecodingReadDto;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * decoding扩展类
 *
 * @Author jiquanwei
 * @Date 2022/9/14 8:53 PM
 **/
public class StreamDecodingExtensions{

    public static YjsDecoder createDecoder(int[] arr){
        return  new YjsDecoder(arr);
    }

    public static int readVarUint(YjsDecoder decoder) throws Exception {
        int num = 0;
        int len = 0;
        int mult = 1;
        while (true) {
            int r = decoder.getArr()[decoder.getPos()];  decoder.addpos();
//            num = num | ((r & BitContanst.BITS7) << len);
            num = num + (r & BitContanst.BITS7) * mult;
            mult *= 128;
            if(r < BitContanst.BIT8){
                return num;
            }
            if(num > Integer.MAX_VALUE){
                throw new Exception("int out of range!");
            }
//            len += 7;
//            if (r < BitContanst.BIT8) {
//                return num >>> 0;
//            }
//            if (len > 53) {
//                throw new Exception("int out of range!");
//            }
        }
    }

    public static int readVarInt(YjsDecoder decoder) throws Exception{
        int r = decoder.getArr()[decoder.getPos()];
        int num = r & BitContanst.BITS6;
        int mult = 64;
        int sign = (r & BitContanst.BIT7) > 0 ? -1 : 1;
        if ((r & BitContanst.BIT8) == 0) {
            // don't continue reading
            return sign * num;
        }
        while(true){
            r = decoder.getArr()[decoder.getPos()];
            num = num + (r & BitContanst.BITS7) * mult;
            mult *= 128;
            if (r < BitContanst.BIT8) {
                return sign * num;
            }
            if(num > Integer.MAX_VALUE){
                throw new Exception("int out of range!");
            }
        }
    }

    public static int[] readVarUint8Array(YjsDecoder decoder) throws Exception {
        return readUint8Array(decoder,readVarUint(decoder));
    }

    public static int[] readUint8Array(YjsDecoder decoder,int len){
        int[] view = createUint8ArrayViewFromArrayBuffer(decoder.getArr(), decoder.getPos(), len+decoder.getPos());
        decoder.setPos(decoder.getPos() + len);
        return view;
    }

    public static int[] createUint8ArrayViewFromArrayBuffer(int[] arr,int start,int end){
        return Arrays.copyOfRange(arr, start, end);
    }

    public static String readVarString(YjsDecoder decoder) throws Exception {
        int remainingLen = readVarUint(decoder);
        if (remainingLen == 0) {
            return "";
        } else {
            int[] codePoints = decoder.getArr();
            return URLDecoder.decode(
                    ClasspodYjsEncode.escape(new String(codePoints,decoder.getPos(), decoder.getArr().length - decoder.getPos())),"UTF-8");
        }
    }

    public static int readUint8(YjsDecoder decoder){
        return decoder.getArr()[decoder.addpos()];
    }

    public static Object readAny(YjsDecoder decoder){
        return null;
    }

    public static Boolean hasContent(YjsDecoder decoder){
        return decoder.getPos() != decoder.getArr().length;
    }

    public static short readUint16(ByteArrayInputStream input){
        return (short)(readByte(input) + (readByte(input) << 8));
    }

    public static int readUint32(ByteArrayInputStream input){
        return ((readByte(input) + (readByte(input) << 8)) + (readByte(input) << 16) + (readByte(input) << 24)) >> 0;
    }

    public static long readVarUInt(ByteArrayInputStream input){
        long num = 0L;
        long mult = 1L;
        try{
            while (true) {
                int r = input.read();
                num = num + (r & BitContanst.BITS7) * mult;
                mult *= 128;
                if (r < BitContanst.BIT8) {
                    return num; // return unsigned number!
                }
                /* istanbul ignore if */
                if (num > Long.MAX_VALUE) {
                    throw new Exception("long out of range!");
                }
            }
        }catch (Exception e){
            // donothing
        }
        return 0L;
    }

    public static long readVarUIntLong(ByteArrayInputStream input){
        long num = 0;
        long len = 0;
        try{
            while (true) {
                int r = input.read();
                num = num | ((r & BitContanst.BITS7) << len);
                len += 7;
                if (r < BitContanst.BIT8) {
                    return num; // return unsigned number!
                }
                /* istanbul ignore if */
                if (len > 53) {
                    throw new Exception("int out of range!");
                }
            }
        }catch (Exception e){
            // donothing
        }
        return 0;
    }

    public static long readVarInt(ByteArrayInputStream input){
        int r = input.read();
        long num = r & Bits.Bits6;
        long mult = 64;
        long sign = (r & Bit.Bit7) > 0 ? -1 : 1;
        if((r & Bit.Bit8) == 0){
            return sign * num;
        }

        while(true){
            r = input.read();
            num = num + (r & BitContanst.BITS7) * mult;
            mult *= 128;
            if(r < Bit.Bit8){
                return sign * num;
            }
            if(num > Long.MAX_VALUE){
                throw new RuntimeException("Long out of range");
            }
        }
    }

    public static String readVarString(ByteArrayInputStream input){
        long remainingLen = readVarUInt(input);
        if(remainingLen == 0){
            return "";
        }
        byte[] data = readByte(input,remainingLen);
        String result = "";
        try {
//            result = new String(data,"utf-8");
            result = URLDecoder.decode(
                    ClasspodYjsEncode.escape(new String(data,0,(int)remainingLen)),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int readUint8(ByteArrayInputStream input){
        return input.read();
    }

    public static byte[] readVarUint8Array(ByteArrayInputStream input){
        long len = readVarUInt(input);
        return readByte(input,len);
    }

    public static Boolean hasContent(ByteArrayInputStream input){
        return input.available() > 0 ? true : false;
    }

    public static ByteArrayInputStream readVarUint8ArrayAsStream(ByteArrayInputStream input){
        byte[] data = readVarUint8Array(input);
        return new ByteArrayInputStream(data);
    }

    public static Object readAny(ByteArrayInputStream input){
        int type = readUint8(input);
        switch(type){
            case 119:// string
                return readVarString(input);
            case 120:// boolean true
                return true;
            case 121:// boolean false
                return false;
            case 123:// float64
                byte[] bytes = new byte[8];
                readByte(input,bytes);
                byte[] reverArray;
                if(BitConverter.isLittleEndian()){
                    reverArray = ArrayReverse.arrayReverse(bytes);
                }else{
                    reverArray = bytes;
                }
                return BitConverter.toDouble(reverArray,0);
            case 124:// float32
                byte[] fBytes = new byte[4];
                readByte(input,fBytes);
                byte[] reverArrays;
                if(BitConverter.isLittleEndian()){
                    reverArrays = ArrayReverse.arrayReverse(fBytes);
                }else{
                    reverArrays = fBytes;
                }
                return BitConverter.toFloat(reverArrays,0);
            case 125:// integer
                return readVarInt(input);
            case 126:// null
            case 127:// undefined
                return null;
            case 116:// ArrayBuffer
                return readVarUint8Array(input);
            case 117:// Array<object>
                long len = readVarUInt(input);
                List<Object> list = new ArrayList<>((int)len);
                for(int i=0;i<len;i++){
                    list.add(readAny(input));
                }
                return list;
            case 118:// map
                long lens = readVarUInt(input);
                Map<String,Object> obj = new HashMap<>((int)lens);
                for(int i =0;i<lens;i++){
                    String key = readVarString(input);
                    obj.put(key,readAny(input));
                }
                return obj;
            default:
                throw new RuntimeException("unknown object type");
        }
    }

    /**
     * 转化为字节
     * @param input
     * @return
     */
    public static byte readByte(ByteArrayInputStream input){
        if(input.available() > 0){
            int value = input.read();
            return Convert.toByte(value);
        }else{
            throw new RuntimeException("read ByteArrayInputStream error");
        }
    }

    public static byte[] readByte(ByteArrayInputStream input,long count){
        Assert.isTrue(count >= 0);
        byte[] result = new byte[(int)count];
        for(int i=0;i<count;i++){
            int v = readByte(input);
            result[i] = Convert.toByte(v);
        }
        return result;
    }

    /**
     * 读取流字节数是否一致
     * @param input
     * @param buffer
     */
    public static void readByte(ByteArrayInputStream input,byte[] buffer){
        if(buffer.length == 0){
            return;
        }
        if(buffer.length != input.read(buffer,0,buffer.length)){
            throw new RuntimeException("stream length inconsistent.");
        }
    }
}
