package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingSignOnly {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method() {
        logger.info("{}, {}", "Hello", "world");
    }
}
