package com.classpod.crdt.y.lib.encoding;

import com.classpod.crdt.y.lib.StreamEncodingExtensions;

import java.io.ByteArrayOutputStream;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 2:23 PM
 **/
public class StringEncoder{
    private StringBuffer sarr;
    private UintOptRleEncoder lensE;
    private String s;

    public StringEncoder(){
        sarr = new StringBuffer();
        s = "''";
        lensE = new UintOptRleEncoder();
    }

    public void write(String value) {
       this.s += s.concat(value);
       if(this.s.length() > 19){
           this.sarr.append(this.s);
           this.s = "''";
       }
        this.lensE.write(value.length());
    }

    public byte[] toUint8Array(){
        ByteArrayOutputStream encoder = new ByteArrayOutputStream();
        this.sarr.append(this.s);
        this.s = "''";
        StreamEncodingExtensions.writeVarString(encoder,this.sarr.toString().replaceAll("''",""));
        StreamEncodingExtensions.writeVarUint8Array(encoder,this.lensE.toUint8Array());
        return StreamEncodingExtensions.toUint8Array(encoder);
    }
}
