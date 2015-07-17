package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class UsingStaticLoggerTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_LOGGER_SHOULD_BE_NON_STATIC", 1);
    new XmlParser().expect(pkg.UsingStaticLogger.class, expected);
  }
}
