package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 11:05 AM
 **/
public class IncUintOptRleEncoder{
    private long state;
    private long count;
    private ByteArrayOutputStream encoder;

    public IncUintOptRleEncoder(){
        this.encoder = new ByteArrayOutputStream();
        this.state = 0L;
        this.count = 0L;
    }

    public void write(long value) {
        if(state + count == value){
            count++;
        }else{
            flushUintOptRleEncoder(this);
            count = 1;
            state = value;
        }
    }

    public byte[] toUint8Array(){
        flushUintOptRleEncoder(this);
        return StreamEncodingExtensions.toUint8Array(this.encoder);
    }

    private void flushUintOptRleEncoder(IncUintOptRleEncoder encoder){
        if(encoder.count > 0){
            StreamEncodingExtensions.writeVarInt(encoder.encoder,encoder.count == 1L ? encoder.state : -encoder.state);
        }
        if(encoder.count > 1){
            StreamEncodingExtensions.writeVarInt(encoder.encoder,encoder.count -2L);
        }
    }
}
