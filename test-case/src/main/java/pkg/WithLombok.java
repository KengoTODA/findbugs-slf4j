package pkg;

import lombok.extern.slf4j.Slf4j;

public @Slf4j class WithLombok {
  void method(String name) {
    log.info("Hello, {}");
  }
}
