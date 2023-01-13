package com.classpod.crdt.y.lib.encoding;

import lombok.Data;

/**
 *
 *
 * @Author jiquanwei
 * @Date 2022/9/14 10:39 AM
 **/
@Data
public class EncoderDto {
    private byte[] buffer;
    private int length;
}
