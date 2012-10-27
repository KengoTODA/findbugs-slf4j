package jp.skypencil.findbugs.slf4j;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import pkg.UsingCaughtExceptionToLog;

public class UsingCaughtExceptionToLogTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		new XmlParser().expectBugs(UsingCaughtExceptionToLog.class, 0);
	}

}
