package com.classpod.crdt.y.lib.encoding;
/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/9 3:08 PM
 **/
public interface IEncoder<T> {

    void write(T value);

    byte[] toArray();

    EncoderDto getBuffer();
}
