package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 1:33 PM
 **/
public class IntDiffOptRleEncoder{
    private Long state;
    private Long diff;
    private int count = 0;
    private ByteArrayOutputStream encoder;

    public IntDiffOptRleEncoder(){
        this.encoder = new ByteArrayOutputStream();
        this.state = 0L;
        this.diff = 0L;
        this.count = 0;
    }

    public void write(long value) {
        if(diff == value - state){
            this.state = value;
            this.count++;
        }else{
            flushIntDiffOptRleEncoder(this);
            count = 1;
            diff = value - state;
            state = value;
        }
    }

    public byte[] toUint8Array(){
        flushIntDiffOptRleEncoder(this);
        return StreamEncodingExtensions.toUint8Array(this.encoder);
    }

    private void flushIntDiffOptRleEncoder(IntDiffOptRleEncoder encoder){
        if(encoder.count > 0){
            long encodedDiff = encoder.diff * 2 + (encoder.count == 1L ? 0L : 1L);
            StreamEncodingExtensions.writeVarInt(encoder.encoder,encodedDiff);
            if(encoder.count > 1){
                StreamEncodingExtensions.writeVarUint(encoder.encoder,encoder.count - 2L);
            }
        }
    }
}
