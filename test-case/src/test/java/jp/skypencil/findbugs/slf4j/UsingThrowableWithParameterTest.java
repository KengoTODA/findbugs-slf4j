package jp.skypencil.findbugs.slf4j;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import pkg.UsingThrowableWithParameter;

public class UsingThrowableWithParameterTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		new XmlParser().expectBugs(UsingThrowableWithParameter.class, 0);
	}

}
