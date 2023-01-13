package com.classpod.crdt.y.lib.decoding;

public class YjsDecoder {

  private int[] arr;

  private int pos;

  public YjsDecoder(int[] uint8Array){
    this.arr = uint8Array;
    this.pos = 0;
  }

  public int[] getArr() {
    return arr;
  }

  public void setArr(int[] arr) {
    this.arr = arr;
  }

  public int getPos() {
    return pos;
  }

  public void setPos(int pos) {
    this.pos = pos;
  }

  public int addpos(){
    this.pos ++;
    return this.pos;
  }

}