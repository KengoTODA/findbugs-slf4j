package jp.skypencil.findbugs.slf4j;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class Issue29Test {
  @Test
  public void testToFindPlaceHolderMismatch() {
    ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
    new XmlParser().expect(pkg.Issue29.class, builder
            .put("DLS_DEAD_LOCAL_STORE",  1)
            .put("SLF4J_SIGN_ONLY_FORMAT",  1)
            .put("SLF4J_MANUALLY_PROVIDED_MESSAGE",  1)
            .put("UC_USELESS_VOID_METHOD",  1)
            .put("UC_USELESS_OBJECT",  1)
            .build()
    );
  }
}
