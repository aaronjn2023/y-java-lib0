package com.classpod.crdt.y.lib;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/11/4 5:10 PM
 **/
public class ByteToUnIntUtil {

    /**
     * 将byte转化为无符号int
     * @param buf
     * @return
     */
    public static int[] byteToIntArray(byte[] buf){
        int[] array = new int[buf.length];
        for(int i =0;i<buf.length;i++){
            array[i] = Byte.toUnsignedInt(buf[i]);
        }
        return array;
    }

    /**
     * 将int[] 转化为byte[]
     * @param array
     * @return
     */
    public static byte[] unIntToByte(int[] array){
        byte[] bytes = new byte[array.length];
        for(int i = 0;i<array.length;i++){
            bytes[i] = (byte)array[i];
        }
        return bytes;
    }
}
