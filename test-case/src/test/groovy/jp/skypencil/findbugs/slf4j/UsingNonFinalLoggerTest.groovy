package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingNonFinalLoggerTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingNonFinalLogger, [
      'SLF4J_LOGGER_SHOULD_BE_FINAL': 1
    ])
  }
}
