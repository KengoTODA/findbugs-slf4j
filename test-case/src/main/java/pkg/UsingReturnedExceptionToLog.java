package pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UsingReturnedExceptionToLog {
	private Logger logger = LoggerFactory.getLogger(getClass());
	void methodWithClass() {
		Utility utility = new Utility();
		logger.warn("log exception", utility.utility());
		LoggerFactory.getLogger(getClass()).warn("log exception", utility.utility());

		logger.warn("something {}", "happen", utility.utility());
		LoggerFactory.getLogger(getClass()).warn("something {}", "happen", utility.utility());

		logger.warn("{} {}", new Object[]{ "something", "happen", utility.utility() });
		LoggerFactory.getLogger(getClass()).warn("{} {}", new Object[]{ "something", "happen", utility.utility() });
	}

	void methodWithInterface() {
		UtilityInterface utility = new Utility();
		logger.warn("log exception", utility.utility());
		LoggerFactory.getLogger(getClass()).warn("log exception", utility.utility());

		logger.warn("something {}", "happen", utility.utility());
		LoggerFactory.getLogger(getClass()).warn("something {}", "happen", utility.utility());

		logger.warn("{} {}", new Object[]{ "something", "happen", utility.utility() });
		LoggerFactory.getLogger(getClass()).warn("{} {}", new Object[]{ "something", "happen", utility.utility() });
	}

	private static interface UtilityInterface {
		Throwable utility();
	}

	private static class Utility implements UtilityInterface {
		@Override
		public Throwable utility() {
			return new RuntimeException();
		}
	}
}
