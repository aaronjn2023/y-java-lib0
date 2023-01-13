package com.classpod.crdt.y.lib.encoding;

import java.util.ArrayList;
import java.util.List;

public class YjsEncoder {

  private int cpos;

  private int[] cbuf;

  private List<int[]> bufs;

  public YjsEncoder() {
    this.cpos = 0;
    this.cbuf = new int[100];
    this.bufs = new ArrayList();
  }


  public int getCpos() {
    return cpos;
  }

  public void setCpos(int cpos) {
    this.cpos = cpos;
  }

  public int[] getCbuf() {
    return cbuf;
  }

  public void setCbuf(int[] cbuf) {
    this.cbuf = cbuf;
  }

  public List<int[]> getBufs() {
    return bufs;
  }

  public void setBufs(List<int[]> bufs) {
    this.bufs = bufs;
  }

  public int addcpos(){
    this.cpos ++;
    return this.cpos;
  }
}