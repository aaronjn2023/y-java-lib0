package com.classpod.crdt.y.lib.decoding;

import cn.hutool.core.lang.Assert;

import java.io.ByteArrayInputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 4:05 PM
 **/
public abstract class AbstractStreamDecoder<T> implements IDecoder<T>{

    public ByteArrayInputStream byteArrayinputStream;
    public AbstractStreamDecoder(ByteArrayInputStream input){
        Assert.isTrue(input != null);
        this.byteArrayinputStream = input;
    }

    protected boolean hasContent(){
        return byteArrayinputStream.read() > 0 ? true : false;
    }

    @Override
    public abstract T read();
}
