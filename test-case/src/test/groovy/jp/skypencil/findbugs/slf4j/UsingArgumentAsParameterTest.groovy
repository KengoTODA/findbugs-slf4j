package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingArgumentAsParameterTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingArgumentAsParameter, [
      'SLF4J_UNKNOWN_ARRAY': 2
    ])
  }
}
