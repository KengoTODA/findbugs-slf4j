package pkg;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * <p>A simple test case to explain how to use {@link XmlParser} to test.
 *
 * @author eller86
 */
public class MessageOnlyTest {

	/**
	 * <p>A simple test method to check that MessageOnly.java has no bug.
	 */
	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		new XmlParser().expectBugs(MessageOnly.class, 0);
	}

}
