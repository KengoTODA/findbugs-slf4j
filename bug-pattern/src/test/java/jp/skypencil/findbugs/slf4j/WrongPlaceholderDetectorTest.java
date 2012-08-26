package jp.skypencil.findbugs.slf4j;

import static org.junit.Assert.*;

import static org.hamcrest.core.Is.is;
import org.junit.Test;

public class WrongPlaceholderDetectorTest {
	@Test
	public void testCountPlaceholders() {
		WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
		assertThat(detector.countPlaceholder(""), is(0));
		assertThat(detector.countPlaceholder("{}"), is(1));
		assertThat(detector.countPlaceholder("Hello, world!"), is(0));
		assertThat(detector.countPlaceholder("{}, {}!"), is(2));
		assertThat(detector.countPlaceholder("\\{}"), is(0));
	}

	@Test
	public void testIndexOf() {
		WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
		assertThat(detector.indexOf("(Ljava/lang/String;)V", "Ljava/lang/String;"), is(0));
		assertThat(detector.indexOf("(Ljava/lang/String;Ljava/lang/String;)V", "Ljava/lang/String;"), is(1));
		assertThat(detector.indexOf("(Ljava/lang/String;Ljava/lang/Throwable;)V", "Ljava/lang/String;"), is(1));
		assertThat(detector.indexOf("(I,Ljava/lang/String;)V", "Ljava/lang/String;"), is(0));
		assertThat(detector.indexOf("(Ljava/lang/String;)V", "Ljava/lang/Throwable;"), is(-1));
	}

	@Test
	public void testCountParameterWithoutArray() {
		WrongPlaceholderDetector detector = new WrongPlaceholderDetector(null);
		assertThat(detector.countParameter(null, "(Ljava/lang/String;)V"), is(0));
		assertThat(detector.countParameter(null, "(Ljava/lang/String;Ljava/lang/Object;)V"), is(1));
		assertThat(detector.countParameter(null, "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), is(2));
		assertThat(detector.countParameter(null, "(Lorg/slf4j/Maker;Ljava/lang/String;Ljava/lang/Object;)V"), is(1));
		assertThat(detector.countParameter(null, "(Lorg/slf4j/Maker;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), is(2));
	}
}
