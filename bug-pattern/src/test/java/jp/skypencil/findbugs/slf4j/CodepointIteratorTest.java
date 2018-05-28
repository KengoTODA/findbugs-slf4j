package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class CodepointIteratorTest {
  @Test
  public void testAbc() {
    CodepointIterator iter = new CodepointIterator("abc");
    assertThat(iter.next()).isEqualTo(Integer.valueOf('a'));
    assertThat(iter.next()).isEqualTo(Integer.valueOf('b'));
    assertThat(iter.next()).isEqualTo(Integer.valueOf('c'));
  }

  @Test
  public void testSurrogatePair() {
    CodepointIterator iter = new CodepointIterator("a𠮟c");
    int expected = ("𠮟".charAt(0) << 16) + "𠮟".charAt(1);

    assertThat(iter.next()).isEqualTo(Integer.valueOf('a'));
    assertThat(iter.next()).isEqualTo(Integer.valueOf(expected));
    assertThat(iter.next()).isEqualTo(Integer.valueOf('c'));
  }
}
