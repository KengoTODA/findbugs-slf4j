package pkg;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class UsingOnePlaceHolderTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		new XmlParser().expectBugs(UsingOnePlaceHolder.class.getSimpleName(), 0);
	}

}
