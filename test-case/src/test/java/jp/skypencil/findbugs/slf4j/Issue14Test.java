package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class Issue14Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    new XmlParser().expect(pkg.Issue14.class, Collections.emptyMap());
  }
}