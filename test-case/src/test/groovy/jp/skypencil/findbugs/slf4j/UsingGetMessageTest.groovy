package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingGetMessageTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingGetMessage, [
      'SLF4J_MANUALLY_PROVIDED_MESSAGE': 3
    ])
  }
}
