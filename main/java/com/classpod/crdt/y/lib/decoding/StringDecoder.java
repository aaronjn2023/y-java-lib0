package com.classpod.crdt.y.lib.decoding;

import com.classpod.crdt.y.lib.StreamDecodingExtensions;

/**
 * xxx
 *
 * @Author jiquanwei
 * @Date 2022/9/14 5:25 PM
 **/
public class StringDecoder{
    private UintOptRleDecoder decoder;
    private String str;
    private int spos;

    public StringDecoder(byte[] uint8Array){
        this.str = StreamDecodingExtensions.readVarString(this.decoder);
        this.decoder = new UintOptRleDecoder(uint8Array);
        this.spos = 0;
    }

    public String reads() {
        long end = this.spos + this.decoder.reads();
        String res = this.str.substring(this.spos,(int)end);
        this.spos = (int)end;
        return res;
    }
}
