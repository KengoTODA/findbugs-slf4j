package jp.skypencil.findbugs.slf4j;

import java.util.Collections;

import org.junit.Test;

public class UsingConstantAsFormatTest {
  @Test
  public void test() {
    new XmlParser().expect(pkg.UsingConstantAsFormat.class, Collections.emptyMap());
  }
}
