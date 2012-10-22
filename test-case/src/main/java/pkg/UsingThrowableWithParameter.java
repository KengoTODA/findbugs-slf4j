package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingThrowableWithParameter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	void method() {
		logger.info("{}", "using", new RuntimeException());
		LoggerFactory.getLogger(getClass()).info("{}", "using", new RuntimeException());
	}

}
