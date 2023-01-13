package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 1:53 PM
 **/
public class RleEncoder extends ByteArrayOutputStream {
    private Long state;
    private int count = 0;
    public RleEncoder(){
        super();
        this.state = null;
        this.count = 0;
    }

    public void write(long value) {
        if(this.state.longValue() == value){
            this.count++;
        }else{
            if(this.count > 0){
                StreamEncodingExtensions.writeVarUint(this,this.count -1L);
            }
            count = 1;
            // todo 这里根据实际调用情况,直接使用writeUint8方法
            // this.infoEncoder = new encoding.RleEncoder(encoding.writeUint8)
            StreamEncodingExtensions.writeUint8(this,(int)value);
            this.state = value;
        }
    }
}
