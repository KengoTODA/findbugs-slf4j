package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingStaticLoggerTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingStaticLogger, [
      'SLF4J_LOGGER_SHOULD_BE_NON_STATIC': 1
    ])
  }
}
