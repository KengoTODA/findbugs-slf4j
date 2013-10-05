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

    void method(String requestId) {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            if (e instanceof IllegalStateException
                    || e instanceof IllegalArgumentException
                    || e.getCause() instanceof IllegalStateException) {
                logger.error("Failed on requestId={} due to an exception ",
                        requestId, e);
            } else {
                throw e;
            }
        }
    }
}
