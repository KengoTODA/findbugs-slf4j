package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingJapaneseTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingJapanese.class, Collections.emptyMap());
  }
}
