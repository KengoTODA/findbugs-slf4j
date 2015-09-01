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
        logMessageStatic(MSG);
        logMessagePrivateCalledWithNonConstantValue(MSG);
        logMessagePrivateCalledWithNonConstantValue(this.toString());
        logMessageStaticPrivateCalledWithNonConstantValue(MSG);
        logMessageStaticPrivateCalledWithNonConstantValue(this.toString());
    }

    // TODO count {} in constant value

    public void logMessagePublic(String messageFromParameter) {
        log.info(messageFromParameter); // NG, this is not private method
    }

    void logMessagePackage(String messageFromParameter) {
        log.info(messageFromParameter); // NG, this is not private method
    }

    private void logMessage(String messageFromParameter) {
        log.info(messageFromParameter); // OK, because this parameter always constant value
    }

    private static void logMessageStatic(String messageFromParameter) {
        LoggerFactory.getLogger(Issue35.class).info(messageFromParameter); // OK, because this parameter always constant value
    }

    private static void logMessageStaticPrivateCalledWithNonConstantValue(String messageFromParameter) {
        LoggerFactory.getLogger(Issue35.class).info(messageFromParameter); // NG, because this parameter is not constant value
    }

    private void logMessagePrivateCalledWithNonConstantValue(String messageFromParameter) {
        log.info(messageFromParameter); // NG, because this parameter is not constant value
    }
}
