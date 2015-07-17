package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class LdcwToLoadStringTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.LdcwToLoadString.class, Collections.emptyMap());
  }
}
