package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingThrowableWithTwoParametersTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingThrowableWithTwoParameters, [:])
  }
}
