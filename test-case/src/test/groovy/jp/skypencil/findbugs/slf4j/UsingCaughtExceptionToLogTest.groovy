package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingCaughtExceptionToLogTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingCaughtExceptionToLog, [:])
  }
}
