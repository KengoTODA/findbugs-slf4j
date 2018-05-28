package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Issue14Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    Map<String, Integer> expected = Collections.emptyMap();
    new XmlParser().expect(pkg.Issue14.class, expected);
  }
}
