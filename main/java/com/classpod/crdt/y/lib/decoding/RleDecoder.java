package com.classpod.crdt.y.lib.decoding;

import com.classpod.crdt.y.lib.StreamDecodingExtensions;

import java.io.ByteArrayInputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 5:06 PM
 **/
public class RleDecoder extends ByteArrayInputStream{
    private Long state;
    private Long count;
    private ByteArrayInputStream reader;

    public RleDecoder(byte[] uint8Array,ByteArrayInputStream input) {
        super(uint8Array);
        this.reader = input;
        this.state = null;
        this.count = 0L;
    }

    public Long reads() {
        if(count == 0){
            this.state = (long)StreamDecodingExtensions.readUint8(this);;
        }
        if(StreamDecodingExtensions.hasContent(this)){
            this.count = StreamDecodingExtensions.readVarUInt(this) + 1;
        }else{
            this.count = -1L;
        }
        count--;
        return this.state;
    }
}
