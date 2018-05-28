package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingJapanese {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  void method() {
    logger.info("ログを出力します。");
    logger.warn("ここで𠮟などのサロゲートペアを出力します。");
  }
}
