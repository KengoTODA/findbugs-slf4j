package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingReturnedExceptionToLogTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingReturnedExceptionToLog, [:])
  }
}
