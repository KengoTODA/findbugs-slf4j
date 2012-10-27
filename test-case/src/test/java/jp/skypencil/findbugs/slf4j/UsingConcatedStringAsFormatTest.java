package jp.skypencil.findbugs.slf4j;

import org.junit.Test;

import pkg.UsingConcatedStringAsFormat;

public class UsingConcatedStringAsFormatTest {

	@Test
	public void test() {
		new XmlParser().expectBugs(UsingConcatedStringAsFormat.class, 4);
	}

}
