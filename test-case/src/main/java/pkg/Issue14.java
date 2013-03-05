package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class to reproduce issue 14.
 *
 * @see https://github.com/eller86/findbugs-slf4j/issues/14
 */
public class Issue14 {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    void makingExceptionByPrivateMethod() {
        RuntimeException e = makeException();
        logger.error("Error ocurred", e);
        logger.error("Error ocurred: {}", e.getMessage(), e);
    }


    void makingExceptionByStaticMethod() {
        RuntimeException e = makeExceptionStatic();
        logger.error("Error ocurred", e);
        logger.error("Error ocurred: {}", e.getMessage(), e);
    }

    private RuntimeException makeException() {
        return new RuntimeException();
    }

    private static RuntimeException makeExceptionStatic() {
        return new RuntimeException();
    }
}
