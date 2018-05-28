package jp.skypencil.findbugs.slf4j;

import java.util.Iterator;

final class CodepointIterator implements Iterator<Integer> {
  private CharSequence sequence;
  private int index;

  CodepointIterator(CharSequence sequence) {
    this.sequence = sequence;
  }

  @Override
  public boolean hasNext() {
    return index < sequence.length();
  }

  @Override
  public Integer next() {
    final int result;
    if (Character.isHighSurrogate(sequence.charAt(index))) {
      result = (sequence.charAt(index) << 16) + sequence.charAt(index + 1);
      index += 2;
    } else {
      result = sequence.charAt(index);
      index += 1;
    }
    return Integer.valueOf(result);
  }

  @Override
  @Deprecated
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
