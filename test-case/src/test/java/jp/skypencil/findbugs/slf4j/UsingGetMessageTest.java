package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class UsingGetMessageTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_MANUALLY_PROVIDED_MESSAGE", 3);
    new XmlParser().expect(pkg.UsingGetMessage.class, expected);
  }
}
