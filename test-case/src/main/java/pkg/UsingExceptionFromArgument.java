package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingExceptionFromArgument extends Exception {
  private static final long serialVersionUID = -5429759466846069392L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  // use aload_0 to load exception
  public void useAload0() {
    logger.error("Hello, {}", "world", this);
  }

  // use aload_1 to load exception
  public void useAload1(Exception one) {
    logger.error("Hello, {}", "world", one);
  }

  // use aload_2 to load exception
  public void useAload2(String one, Exception two) {
    logger.error("Hello, {}", "one", two);
  }

  // use aload_3 to load exception
  public void useAload3(String one, int two, Exception three) {
    logger.error("Hello, {} and {}", new Object[] {one, two, three});
  }

  // use aload to load exception
  public void useAload(String one, int two, byte[] three, Exception four) {
    logger.error("Hello {}, {} and {}", new Object[] {one, two, three, four});
  }

  public static void staticMethod(Exception e) {
    LoggerFactory.getLogger(UsingExceptionFromArgument.class).error("Hello, {}", "world", e);
  }

  public static void useAloadStatic(Object one, Object two, Object three, Exception four) {
    LoggerFactory.getLogger(UsingExceptionFromArgument.class).error("Hello, {}", "world", four);
  }
}
