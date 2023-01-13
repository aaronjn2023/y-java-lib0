package com.classpod.crdt.y.lib.decoding;

import com.classpod.crdt.y.lib.Bit;
import com.classpod.crdt.y.lib.StreamDecodingExtensions;

import java.io.ByteArrayInputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 4:59 PM
 **/
public class IntDiffOptRleDecoder extends ByteArrayInputStream{
    private long state;
    private long count;
    private long diff;

    public IntDiffOptRleDecoder(byte[] uint8Array) {
        super(uint8Array);
        this.state = 0L;
        this.count = 0L;
        this.diff = 0L;
    }

    public Long reads() {
        if(count == 0){
            long diff = StreamDecodingExtensions.readVarInt(this);
            long hasCount = (diff & Bit.Bit1);
            this.diff = diff>>1;
            this.count = 1L;
            if(hasCount > 0){
                this.count = StreamDecodingExtensions.readVarUInt(this) + 2;
            }
        }

        this.state += this.diff;
        this.count--;
        return this.state;
    }
}
