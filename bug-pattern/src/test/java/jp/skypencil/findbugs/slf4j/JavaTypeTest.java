package jp.skypencil.findbugs.slf4j;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.apache.bcel.generic.Type;
import org.junit.Test;

public class JavaTypeTest {
    /**
     * @throws ClassNotFoundException 
     * @see https://github.com/eller86/findbugs-slf4j/issues/17
     */
    @Test
    public void testIssue17() throws ClassNotFoundException {
        assertThat(JavaType.from(Type.BOOLEAN), is(nullValue()));
        assertThat(JavaType.from(Type.BYTE), is(nullValue()));
        assertThat(JavaType.from(Type.CHAR), is(nullValue()));
        assertThat(JavaType.from(Type.SHORT), is(nullValue()));
        assertThat(JavaType.from(Type.INT), is(nullValue()));
        assertThat(JavaType.from(Type.LONG), is(nullValue()));
        assertThat(JavaType.from(Type.FLOAT), is(nullValue()));
        assertThat(JavaType.from(Type.DOUBLE), is(nullValue()));
    }
}
