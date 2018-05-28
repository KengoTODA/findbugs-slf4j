package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

public class JavaTypeTest {
  /**
   * @throws ClassNotFoundException
   * @see https://github.com/KengoTODA/findbugs-slf4j/issues/17
   */
  @Test
  public void testIssue17() throws ClassNotFoundException {
    assertThat(JavaType.from(Type.BOOLEAN)).isNull();
    assertThat(JavaType.from(Type.BYTE)).isNull();
    assertThat(JavaType.from(Type.CHAR)).isNull();
    assertThat(JavaType.from(Type.SHORT)).isNull();
    assertThat(JavaType.from(Type.INT)).isNull();
    assertThat(JavaType.from(Type.LONG)).isNull();
    assertThat(JavaType.from(Type.FLOAT)).isNull();
    assertThat(JavaType.from(Type.DOUBLE)).isNull();
  }
}
