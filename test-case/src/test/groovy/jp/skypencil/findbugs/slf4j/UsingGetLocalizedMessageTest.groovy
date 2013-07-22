package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingGetLocalizedMessageTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingGetLocalizedMessage, [
      'SLF4J_MANUALLY_PROVIDED_MESSAGE': 3
    ])
  }
}
