package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** A simple test case to explain how to use {@link XmlParser} to test. */
public class MessageOnlyTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.emptyMap();
    new XmlParser().expect(pkg.MessageOnly.class, expected);
  }
}
