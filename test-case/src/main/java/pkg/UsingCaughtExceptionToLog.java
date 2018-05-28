package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingCaughtExceptionToLog {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    try {
      System.out.println("Hello, world");
    } catch (RuntimeException e) {
      logger.warn("log exception", e);
      LoggerFactory.getLogger(getClass()).warn("log exception", e);

      logger.warn("something {}", "happen", e);
      LoggerFactory.getLogger(getClass()).warn("something {}", "happen", e);

      logger.warn("{} has been {}", new Object[] {"something", "happened", e});
      LoggerFactory.getLogger(getClass())
          .warn("{} has been {}", new Object[] {"something", "happened", e});
    }
  }
}
