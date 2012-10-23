package pkg;

import org.junit.Test;

public class UsingGivenArrayAsParameterTest {

	@Test
	public void test() {
		new XmlParser().expectBugs(UsingGivenArrayAsParameter.class, 2);
	}

}
