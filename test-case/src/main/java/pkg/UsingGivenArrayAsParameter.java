package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsingGivenArrayAsParameter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	void method(Object[] givenArray) {
		logger.info("Given parameter is {}", givenArray);
		LoggerFactory.getLogger(getClass()).info("Given parameter is {}", givenArray);
	}
}
