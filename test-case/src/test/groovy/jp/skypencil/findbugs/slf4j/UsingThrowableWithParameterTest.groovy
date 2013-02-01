package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingThrowableWithParameterTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingThrowableWithParameter, [:])
  }
}
