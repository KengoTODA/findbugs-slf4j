package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingInnerClassTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingInnerClass, [
      'SLF4J_ILLEGAL_PASSED_CLASS': 2
    ])
  }
}
