package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingOtherClassTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingOtherClass, [
      'SLF4J_ILLEGAL_PASSED_CLASS': 2
    ])
  }
}
