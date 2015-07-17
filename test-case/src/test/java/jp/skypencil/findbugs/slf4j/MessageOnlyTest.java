package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

/**
 * A simple test case to explain how to use {@link XmlParser} to test.
 */
public class MessageOnlyTest {
  @Test
  public void test() {
      new XmlParser().expect(pkg.MessageOnly.class, Collections.emptyMap());
  }
}
