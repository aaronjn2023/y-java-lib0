package com.classpod.crdt.y.lib.encoding;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 10:46 AM
 **/
public abstract class AbstractStreamEncoder<T> implements IEncoder<T>{

    public ByteArrayOutputStream byteArrayOutputStream;

    public AbstractStreamEncoder(){
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public abstract void write(T value);

    @Override
    public byte[] toArray(){
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public EncoderDto getBuffer(){
        EncoderDto encoderDto = new EncoderDto();
        encoderDto.setBuffer(byteArrayOutputStream.toByteArray());
        encoderDto.setLength(byteArrayOutputStream.size());
        return encoderDto;
    }
}
