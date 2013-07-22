package jp.skypencil.findbugs.slf4j.parameter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static jp.skypencil.findbugs.slf4j.parameter.AbstractDetectorForParameterArray.indexOf;

import org.junit.Test;

public class AbstractDetectorForParameterArrayTest {

    @Test
    public void testIndexOf() {
        assertThat(indexOf("(Ljava/lang/String;)V", "Ljava/lang/String;"), is(0));
        assertThat(indexOf("(Ljava/lang/String;Ljava/lang/String;)V", "Ljava/lang/String;"), is(1));
        assertThat(indexOf("(Ljava/lang/String;Ljava/lang/Throwable;)V", "Ljava/lang/String;"), is(1));
        assertThat(indexOf("(Ljava/lang/Object;Ljava/lang/String;)V", "Ljava/lang/String;"), is(0));
        assertThat(indexOf("(Ljava/lang/String;)V", "Ljava/lang/Throwable;"), is(-1));
    }

}
