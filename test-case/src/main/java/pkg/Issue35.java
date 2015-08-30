package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://github.com/KengoTODA/findbugs-slf4j/issues/35#issuecomment-135973699
 */
public class Issue35 {
    private static final String MSG = "{}: abcd={}"
            + ", really long text forcing line wraps for formatting={}"
            + ", y={}";

    private final Logger log = LoggerFactory.getLogger(getClass());

    void logMessage(final String a, final String b, final MyClass c) {
        final Y y = c.getX().getY();
        log.error(MSG, new Object[]{"literal", a, b, y});
    }

    private static interface X {
        Y getY();
    }

    private static interface Y {
    }

    private static abstract class MyClass {
        abstract X getX();
    }
}
