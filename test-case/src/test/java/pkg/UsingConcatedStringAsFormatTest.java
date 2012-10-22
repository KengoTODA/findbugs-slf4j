package pkg;

import org.junit.Test;

public class UsingConcatedStringAsFormatTest {

	@Test
	public void test() {
		new XmlParser().expectBugs(UsingConcatedStringAsFormat.class, 4);
	}

}
