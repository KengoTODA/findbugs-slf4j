package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingGivenArrayAsParameterTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingGivenArrayAsParameter, [
      'SLF4J_UNKNOWN_ARRAY': 2
    ])
  }
}
