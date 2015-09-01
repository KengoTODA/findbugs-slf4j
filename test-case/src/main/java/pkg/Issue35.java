package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://github.com/KengoTODA/findbugs-slf4j/issues/35
 */
public class Issue35 {
    private static final String MSG = "CONSTANT VALUE";

    private final Logger log = LoggerFactory.getLogger(getClass());

    public void message() {
        logMessage(MSG);
    }

    private void logMessage(String messageFromParameter) {
        log.info(messageFromParameter);
    }
}
