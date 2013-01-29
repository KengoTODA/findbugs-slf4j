package jp.skypencil.findbugs.slf4j;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CodepointIteratorTest {
    @Test
    public void testAbc() {
        CodepointIterator iter = new CodepointIterator("abc");
        assertThat(iter.next(), is(equalTo(Integer.valueOf('a'))));
        assertThat(iter.next(), is(equalTo(Integer.valueOf('b'))));
        assertThat(iter.next(), is(equalTo(Integer.valueOf('c'))));
    }

    @Test
    public void testSurrogatePair() {
        CodepointIterator iter = new CodepointIterator("a𠮟c");
        int expected = ("𠮟".charAt(0) << 16) + "𠮟".charAt(1);

        assertThat(iter.next(), is(equalTo(Integer.valueOf('a'))));
        assertThat(iter.next(), is(equalTo(Integer.valueOf(expected))));
        assertThat(iter.next(), is(equalTo(Integer.valueOf('c'))));
    }
}
