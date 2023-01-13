package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 1:57 PM
 **/
public class RleIntDiffEncoder extends ByteArrayOutputStream{
    private long state;
    private int count;

    public RleIntDiffEncoder(long start){
        super();
        this.state = start;
        this.count = 0;
    }

    public void write(Long value) {
        if(state == value.longValue() && count > 0){
            count++;
        }else{
            if(count > 0){
                StreamEncodingExtensions.writeVarUint(this,this.count -1L);
            }
            this.count = 1;
            StreamEncodingExtensions.writeVarInt(this,value.longValue() - state);
            state = value;
        }
    }
}
