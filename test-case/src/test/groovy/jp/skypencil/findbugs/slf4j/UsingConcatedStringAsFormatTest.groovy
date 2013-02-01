package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingConcatedStringAsFormatTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingConcatedStringAsFormat, [
      'SLF4J_FORMAT_SHOULD_BE_CONST': 4
    ])
  }
}
