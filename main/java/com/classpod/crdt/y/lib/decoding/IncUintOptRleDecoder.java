package com.classpod.crdt.y.lib.decoding;

import com.classpod.crdt.y.lib.StreamDecodingExtensions;

import java.io.ByteArrayInputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 4:25 PM
 **/
public class IncUintOptRleDecoder extends ByteArrayInputStream{
    private long state;
    private long count;

    public IncUintOptRleDecoder(byte[] uint8Array) {
        super(uint8Array);
        this.state = 0L;
        this.count = 0L;
    }

    public Long reads() {
        if(count == 0){
            this.state = StreamDecodingExtensions.readVarInt(this);
            boolean isNegative = this.state < 0;
            if(isNegative){
                state = -this.state;
                count = StreamDecodingExtensions.readVarUInt(this) + 2;
            }
        }
        count--;
        return state++;
    }
}
