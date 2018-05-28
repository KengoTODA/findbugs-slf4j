package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @see https://github.com/KengoTODA/findbugs-slf4j/issues/35 */
public class Issue35 {
  private static final String MSG = "CONSTANT VALUE";

  private final Logger log = LoggerFactory.getLogger(getClass());

  public void testConstantMessagePassedViaArgument() {
    logMessage(MSG);
    logMessageStatic(MSG);
    logMessagePrivateCalledWithNonConstantValue(MSG);
    logMessagePrivateCalledWithNonConstantValue(this.toString());
    logMessageStaticPrivateCalledWithNonConstantValue(MSG);
    logMessageStaticPrivateCalledWithNonConstantValue(this.toString());
  }

  public void testParameterAndThrowable() {
    logMessageWithParameter("CONSTANT VALUE WITH PLACE HOLDER {}", this.toString());
    logMessageWithInvalidParameter("CONSTANT VALUE WITH PLACE HOLDER {} {}", this.toString());
    logMessageWithException(MSG, new RuntimeException());
    logMessageWithInvalidException("CONSTANT VALUE WITH PLACE HOLDER {}", new RuntimeException());
  }

  public void logMessagePublic(String messageFromParameter) {
    log.info(messageFromParameter); // SLF4J_FORMAT_SHOULD_BE_CONST, this is not private method
  }

  void logMessagePackage(String messageFromParameter) {
    log.info(messageFromParameter); // SLF4J_FORMAT_SHOULD_BE_CONST, this is not private method
  }

  private void logMessage(String messageFromParameter) {
    log.info(messageFromParameter); // OK, because this parameter always constant value
  }

  private static void logMessageStatic(String messageFromParameter) {
    LoggerFactory.getLogger(Issue35.class)
        .info(messageFromParameter); // OK, because this parameter always constant value
  }

  private static void logMessageStaticPrivateCalledWithNonConstantValue(
      String messageFromParameter) {
    LoggerFactory.getLogger(Issue35.class)
        .info(
            messageFromParameter); // SLF4J_FORMAT_SHOULD_BE_CONST, because this parameter is not
                                   // constant value
  }

  private void logMessagePrivateCalledWithNonConstantValue(String messageFromParameter) {
    log.info(
        messageFromParameter); // SLF4J_FORMAT_SHOULD_BE_CONST, because this parameter is not
                               // constant value
  }

  private void logMessageWithParameter(String messageFromParameter, String data) {
    log.info(messageFromParameter, data); // OK
  }

  private void logMessageWithInvalidParameter(String messageFromParameter, String data) {
    log.info(
        messageFromParameter,
        data); // SLF4J_PLACE_HOLDER_MISMATCH, because this parameter has two placeholders but we
               // gave only one data
  }

  private void logMessageWithException(String messageFromParameter, Throwable throwable) {
    log.info(messageFromParameter, throwable); // OK
  }

  private void logMessageWithInvalidException(String messageFromParameter, Throwable throwable) {
    log.info(
        messageFromParameter,
        throwable); // SLF4J_PLACE_HOLDER_MISMATCH, because this parameter has one placeholder but
                    // we gave no data
  }
}
