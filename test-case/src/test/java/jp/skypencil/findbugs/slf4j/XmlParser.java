package jp.skypencil.findbugs.slf4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XmlParser {

	void expectBugs(Class<?> clazz, int expectedBugs) {
		final String className = clazz.getSimpleName();
		final String filePath = "pkg/" + className + ".java";
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse("target/findbugsXml.xml");
			NodeList fileStats = doc.getElementsByTagName("FileStats");

			for (int i = 0; i < fileStats.getLength(); ++i) {
				Node node = fileStats.item(i);
				NamedNodeMap attr = node.getAttributes();
				if (attr.getNamedItem("path").getNodeValue().equals(filePath)) {
					final String bugCount = attr.getNamedItem("bugCount").getNodeValue();
					final String message;
					if (expectedBugs == 0) {
						message = filePath + " should have no bug";
					} else {
						message = filePath + " should have " + expectedBugs + " bugs";
					}

					assertThat(message, bugCount, is(Integer.toString(expectedBugs)));
					return;
				}
			}

			fail("Result of " + filePath + " not found");
		} catch (ParserConfigurationException e) {
			throw new AssertionError(e);
		} catch (SAXException e) {
			throw new AssertionError(e);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

}
