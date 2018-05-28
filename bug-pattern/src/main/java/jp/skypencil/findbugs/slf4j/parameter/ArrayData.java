package jp.skypencil.findbugs.slf4j.parameter;

import com.google.common.base.MoreObjects;

public class ArrayData {
  private final int size;
  private boolean mark;

  ArrayData(int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }

  public void mark(boolean mark) {
    this.mark = mark;
  }

  public boolean isMarked() {
    return mark;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("size", size).add("marked", mark).toString();
  }
}
