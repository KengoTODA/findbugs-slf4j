package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingThrowableWithParameterTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingThrowableWithParameter.class, Collections.emptyMap());
  }
}
