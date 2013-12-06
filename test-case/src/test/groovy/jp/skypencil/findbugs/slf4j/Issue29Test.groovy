package jp.skypencil.findbugs.slf4j

import org.junit.Test

class Issue29Test {
  @Test
  void testToFindPlaceHolderMismatch() {
    new XmlParser().expect(pkg.Issue29, [:]);
  }
}