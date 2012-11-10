package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class to reproduce issue 2, but it works.
 *
 * @see https://github.com/eller86/findbugs-slf4j/issues/2
 */
public class Issue2 {
    private Logger logger = LoggerFactory.getLogger(getClass());

    void method() {
        final RuntimeException ex = new RuntimeException();
        final Integer num = 4;
        final String str = "fred";
        logger.debug(str + " barney={}, {}, {}, {}", num, ex);
    }
}
