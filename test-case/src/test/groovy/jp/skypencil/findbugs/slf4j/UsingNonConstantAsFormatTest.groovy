package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingNonConstantAsFormatTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingNonConstantAsFormat, [
      'SLF4J_FORMAT_SHOULD_BE_CONST': 2
    ])
  }
}
