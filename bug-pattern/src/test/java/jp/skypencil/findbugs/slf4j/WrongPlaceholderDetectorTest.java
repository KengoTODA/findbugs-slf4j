package jp.skypencil.findbugs.slf4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import jp.skypencil.findbugs.slf4j.parameter.ThrowableHandler;

import org.junit.Test;

import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.Item;

public class WrongPlaceholderDetectorTest {
    @Test
    public void testCountPlaceholders() {
        WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
        assertThat(detector.countPlaceholder(""), is(0));
        assertThat(detector.countPlaceholder("{}"), is(1));
        assertThat(detector.countPlaceholder("Hello, world!"), is(0));
        assertThat(detector.countPlaceholder("{}, {}!"), is(2));
        assertThat(detector.countPlaceholder("\\{}"), is(0));
    }

    @Test
    public void testCountParameterWithoutArray() {
        WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
        ThrowableHandler throwableHandler = new ThrowableHandler();
        OpcodeStack stack = mock(OpcodeStack.class);

        Item itemInStack = mock(Item.class);
        doReturn(itemInStack).when(stack).getStackItem(0);

        assertThat(detector.countParameter(stack, "(Ljava/lang/String;Ljava/lang/Object;)V", throwableHandler), is(1));
        assertThat(detector.countParameter(stack, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", throwableHandler), is(2));
        assertThat(detector.countParameter(stack, "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;)V", throwableHandler), is(1));
        assertThat(detector.countParameter(stack, "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", throwableHandler), is(2));

        Item exceptionInStack = mock(Item.class);
        doReturn("IS_THROWABLE").when(exceptionInStack).getUserValue();
        doReturn(exceptionInStack).when(stack).getStackItem(0);

        assertThat(detector.countParameter(stack, "(Ljava/lang/String;Ljava/lang/Object;)V", throwableHandler), is(0));
        assertThat(detector.countParameter(stack, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", throwableHandler), is(1));
        assertThat(detector.countParameter(stack, "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;)V", throwableHandler), is(0));
        assertThat(detector.countParameter(stack, "(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", throwableHandler), is(1));
    }
}
