package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingConstantAsFormat {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String CONSTANT = "";
	void method() {
		logger.info(CONSTANT);
		LoggerFactory.getLogger(getClass()).info(CONSTANT);
	}
}
