package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingGetStackTrace {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    void method(RuntimeException ex) {
        logger.error("Failed to determine The Answer to Life, The Universe and Everything: {}; cause: {}", 27, ex.getStackTrace());
    }
}
