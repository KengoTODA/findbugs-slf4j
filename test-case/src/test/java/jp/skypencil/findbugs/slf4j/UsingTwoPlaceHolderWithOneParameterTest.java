package jp.skypencil.findbugs.slf4j;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsingTwoPlaceHolderWithOneParameterTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_PLACE_HOLDER_MISMATCH", 2);
    new XmlParser().expect(pkg.UsingTwoPlaceHolderWithOneParameter.class, expected);
  }
}
