package jp.skypencil.findbugs.slf4j;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.bcel.classfile.ClassFormatException;
import org.junit.jupiter.api.Test;

public class IllegalPassedClassDetectorTest {
    @Test
    public void testFindClass() {
        IllegalPassedClassDetector detector = new IllegalPassedClassDetector(null);
        assertThrows(ClassFormatException.class, () -> {
            detector.findClass("pkg/ClassThatDoesNotExist");
        });
    }
}
