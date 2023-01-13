package com.classpod.crdt.y.lib.decoding;

import com.classpod.crdt.y.lib.StreamDecodingExtensions;

import java.io.ByteArrayInputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 4:55 PM
 **/
public class IntDiffDecoder extends ByteArrayInputStream{
    private long start;

    public IntDiffDecoder(byte[] uint8Array,long start) {
        super(uint8Array);
        this.start = start;
    }

    public Long reads() {
        this.start = StreamDecodingExtensions.readVarInt(this);
        return this.start;
    }
}
