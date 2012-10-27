package jp.skypencil.findbugs.slf4j;

import org.junit.Test;

import pkg.UsingGivenArrayAsParameter;

public class UsingGivenArrayAsParameterTest {

	@Test
	public void test() {
		new XmlParser().expectBugs(UsingGivenArrayAsParameter.class, 2);
	}

}
