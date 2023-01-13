package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 11:50 AM
 **/
public class IntDiffEncoder extends ByteArrayOutputStream{

    private long state;

    public IntDiffEncoder(long start){
        super();
        this.state = start;
    }

    public void write(long value) {
        long diff = value - state;
        StreamEncodingExtensions.writeVarInt(this,diff);
        state = value;
    }
}
