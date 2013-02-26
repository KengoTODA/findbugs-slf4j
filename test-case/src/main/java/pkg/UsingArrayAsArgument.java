package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UsingArrayAsArgument {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    void method(Object[] args) {
        logger.info("Logging int array {}.", int[].class);
    }
}
