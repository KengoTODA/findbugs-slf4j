package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingOnePlaceHolderTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingOnePlaceHolder.class, Collections.emptyMap());
  }
}
