package jp.skypencil.findbugs.slf4j.parameter;

import static com.google.common.truth.Truth.assertThat;
import static jp.skypencil.findbugs.slf4j.parameter.AbstractDetectorForParameterArray.indexOf;

import org.junit.jupiter.api.Test;

public class AbstractDetectorForParameterArrayTest {

  @Test
  public void testIndexOf() {
    assertThat(indexOf("(Ljava/lang/String;)V", "Ljava/lang/String;")).isEqualTo(0);
    assertThat(indexOf("(Ljava/lang/String;Ljava/lang/String;)V", "Ljava/lang/String;"))
        .isEqualTo(1);
    assertThat(indexOf("(Ljava/lang/String;Ljava/lang/Throwable;)V", "Ljava/lang/String;"))
        .isEqualTo(1);
    assertThat(indexOf("(Ljava/lang/Object;Ljava/lang/String;)V", "Ljava/lang/String;"))
        .isEqualTo(0);
    assertThat(indexOf("(Ljava/lang/String;)V", "Ljava/lang/Throwable;")).isEqualTo(-1);
  }
}
