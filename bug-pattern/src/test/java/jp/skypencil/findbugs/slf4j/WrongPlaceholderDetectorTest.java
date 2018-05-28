package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import jp.skypencil.findbugs.slf4j.parameter.ThrowableHandler;
import org.junit.jupiter.api.Test;

public class WrongPlaceholderDetectorTest {
  @Test
  public void testCountPlaceholders() {
    WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
    assertThat(detector.countPlaceholder("")).isEqualTo(0);
    assertThat(detector.countPlaceholder("{}")).isEqualTo(1);
    assertThat(detector.countPlaceholder("Hello, world!")).isEqualTo(0);
    assertThat(detector.countPlaceholder("{}, {}!")).isEqualTo(2);
    assertThat(detector.countPlaceholder("\\{}")).isEqualTo(0);
  }

  @Test
  public void testCountParameterWithoutArray() throws ClassNotFoundException {
    WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
    ThrowableHandler throwableHandler = new ThrowableHandler();
    OpcodeStack stack = mock(OpcodeStack.class);

    Item itemInStack = mock(Item.class);
    doReturn(itemInStack).when(stack).getStackItem(0);

    assertThat(
            detector.countParameter(
                stack, "(Ljava/lang/String;Ljava/lang/Object;)V", throwableHandler))
        .isEqualTo(1);
    assertThat(
            detector.countParameter(
                stack,
                "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                throwableHandler))
        .isEqualTo(2);
    assertThat(
            detector.countParameter(
                stack,
                "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;)V",
                throwableHandler))
        .isEqualTo(1);
    assertThat(
            detector.countParameter(
                stack,
                "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                throwableHandler))
        .isEqualTo(2);

    Item exceptionInStack = mock(Item.class);
    doReturn(ThrowableHandler.THROWABLE).when(exceptionInStack).getJavaClass();
    doReturn(exceptionInStack).when(stack).getStackItem(0);

    assertThat(
            detector.countParameter(
                stack, "(Ljava/lang/String;Ljava/lang/Object;)V", throwableHandler))
        .isEqualTo(0);
    assertThat(
            detector.countParameter(
                stack,
                "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                throwableHandler))
        .isEqualTo(1);
    assertThat(
            detector.countParameter(
                stack,
                "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;)V",
                throwableHandler))
        .isEqualTo(0);
    assertThat(
            detector.countParameter(
                stack,
                "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                throwableHandler))
        .isEqualTo(1);
  }
}
