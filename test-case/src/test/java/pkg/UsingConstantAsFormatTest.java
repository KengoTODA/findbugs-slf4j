package pkg;

import org.junit.Test;

public class UsingConstantAsFormatTest {

	@Test
	public void test() {
		new XmlParser().expectBugs(UsingConstantAsFormat.class.getSimpleName(), 0);
	}

}
