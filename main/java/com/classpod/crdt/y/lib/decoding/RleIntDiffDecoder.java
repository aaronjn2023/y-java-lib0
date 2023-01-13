package com.classpod.crdt.y.lib.decoding;

import cn.hutool.core.lang.Assert;
import com.classpod.crdt.y.lib.StreamDecodingExtensions;

import java.io.ByteArrayInputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 5:16 PM
 **/
public class RleIntDiffDecoder extends ByteArrayInputStream{
    private long state;
    private long count;

    public RleIntDiffDecoder(byte[] uint8Array, long start){
        super(uint8Array);
        this.state = start;
        this.count = 0;
    }

    public Long reads() {
        if(count == 0){
            this.state += StreamDecodingExtensions.readVarInt(this);
            if(StreamDecodingExtensions.hasContent(this)){
                count = StreamDecodingExtensions.readVarUInt(this) + 1;
                Assert.isTrue(count > 0);
            }else{
                count = -1;
            }
        }
        count--;
        return state;
    }
}
