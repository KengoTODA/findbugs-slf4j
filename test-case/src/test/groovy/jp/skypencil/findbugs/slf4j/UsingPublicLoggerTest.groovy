package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingPublicLoggerTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingPublicLogger, [
      'SLF4J_LOGGER_SHOULD_BE_PRIVATE': 1
    ])
  }
}
