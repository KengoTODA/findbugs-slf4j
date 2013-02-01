package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingReturnedArrayAsParameterTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingReturnedArrayAsParameter, [
      'SLF4J_UNKNOWN_ARRAY': 2
    ])
  }
}
