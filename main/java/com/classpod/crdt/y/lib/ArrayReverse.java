package com.classpod.crdt.y.lib;

/**
 * 数组逆序
 *
 * @Author jiquanwei
 * @Date 2022/9/15 11:27 AM
 **/
public class ArrayReverse {

    /**
     * 数组逆序
     * @param originArray
     * @return
     */
    public static byte[] arrayReverse(byte[] originArray) {
        int len = originArray.length;
        if(len == 0) {
            throw new RuntimeException("origin array is null.");
        }
        byte[] reverseArray = new byte[len];
        for (int i = 0; i < len; i++) {
            reverseArray[i] = originArray[len - i - 1];
        }
        return reverseArray;
    }
}
