package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * We can log LocalizedMessage only when there is not throwable instance in argument.
 *
 * @see https://github.com/KengoTODA/findbugs-slf4j/issues/31
 */
public class Issue31 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    try {
      System.out.println("Some operation");
    } catch (Throwable t) {
      logger.info("Test message is {}", t.getLocalizedMessage());
      logger.info("Test message is {}", new Object[] {t.getLocalizedMessage()});
    }
  }
}
