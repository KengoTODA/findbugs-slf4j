package jp.skypencil.findbugs.slf4j;

import org.apache.bcel.generic.Type;
import org.junit.Test;

public class JavaTypeTest {
    /**
     * @throws ClassNotFoundException 
     * @see https://github.com/eller86/findbugs-slf4j/issues/17
     */
    @Test
    public void testIssue17() throws ClassNotFoundException {
        JavaType intType = new JavaType(Type.INT);
        intType.toJavaClass();
    }
}
