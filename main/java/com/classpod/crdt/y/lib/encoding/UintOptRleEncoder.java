package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 2:24 PM
 **/
public class UintOptRleEncoder{
    private long state;
    private long count;
    private ByteArrayOutputStream encoder;

    public UintOptRleEncoder(){
        this.encoder = new ByteArrayOutputStream();
        this.state = 0L;
        this.count = 0L;
    }

    public void write(long value) {
        if(state == value){
            this.count++;
        }else{
            flushUintOptRleEncoder(this);
            this.count = 1L;
            this.state = value;
        }
    }

    public byte[] toUint8Array(){
        flushUintOptRleEncoder(this);
        return StreamEncodingExtensions.toUint8Array(this.encoder);
    }

    private void flushUintOptRleEncoder(UintOptRleEncoder encoder){
        if(encoder.count > 0L){
            StreamEncodingExtensions.writeVarInt(encoder.encoder,encoder.count == 1L ? encoder.state : -encoder.state);
        }
        if(encoder.count > 1L){
            StreamEncodingExtensions.writeVarInt(encoder.encoder,encoder.count -2L);
        }
    }
}
