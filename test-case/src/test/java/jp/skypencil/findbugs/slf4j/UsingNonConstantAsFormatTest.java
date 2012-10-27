package jp.skypencil.findbugs.slf4j;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import pkg.UsingNonConstantAsFormat;

public class UsingNonConstantAsFormatTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		new XmlParser().expectBugs(UsingNonConstantAsFormat.class, 2);
	}

}
