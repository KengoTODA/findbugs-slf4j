package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class UsingConcatedStringAsFormatTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_FORMAT_SHOULD_BE_CONST", 4);
    new XmlParser().expect(pkg.UsingConcatedStringAsFormat.class, expected);
  }
}
