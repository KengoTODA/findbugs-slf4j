package jp.skypencil.findbugs.slf4j;

import org.junit.Test;

import pkg.UsingConstantAsFormat;

public class UsingConstantAsFormatTest {

	@Test
	public void test() {
		new XmlParser().expectBugs(UsingConstantAsFormat.class, 0);
	}

}
