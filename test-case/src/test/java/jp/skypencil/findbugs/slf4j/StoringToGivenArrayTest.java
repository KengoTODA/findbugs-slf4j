package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class StoringToGivenArrayTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.StoringToGivenArray.class, Collections.emptyMap());
  }
}
