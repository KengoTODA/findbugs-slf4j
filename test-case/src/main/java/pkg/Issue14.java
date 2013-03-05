package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class to reproduce issue 14, but it works.
 *
 * @see https://github.com/eller86/findbugs-slf4j/issues/14
 */
public class Issue14 {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    void method() {
        final RuntimeException e = new RuntimeException();
        logger.error("Error ocurred", e);
        logger.error("Error ocurred: {}", e.getMessage(), e);
    }
}
