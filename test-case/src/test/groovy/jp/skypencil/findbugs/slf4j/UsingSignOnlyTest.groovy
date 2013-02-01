package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingSignOnlyTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingSignOnly, [
      'SLF4J_SIGN_ONLY_FORMAT': 1
    ])
  }
}
