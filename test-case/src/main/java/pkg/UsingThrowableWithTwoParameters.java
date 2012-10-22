package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingThrowableWithTwoParameters {
	private Logger logger = LoggerFactory.getLogger(getClass());
	void method() {
		logger.info("{} {}", new Object[] {"using", "2 parameters", new RuntimeException()});
		LoggerFactory.getLogger(getClass()).info("{} {}", new Object[] {"using", "2 parameters", new RuntimeException()});
	}

}
