package jp.skypencil.findbugs.slf4j

import org.junit.Test

class UsingMarkerTest {
  @Test
  void test() {
    new XmlParser().expect(pkg.UsingMarker, [
      'NP_LOAD_OF_KNOWN_NULL_VALUE': 1
    ]);
  }
}