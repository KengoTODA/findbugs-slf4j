package pkg;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UsingOnePlaceHolderTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse("target/findbugsXml.xml");
		NodeList fileStats = doc.getElementsByTagName("FileStats");

		for (int i = 0; i < fileStats.getLength(); ++i) {
			Node node = fileStats.item(i);
			NamedNodeMap attr = node.getAttributes();
			if (attr.getNamedItem("path").getNodeValue().equals("pkg/UsingOnePlaceHolder.java")) {
				String bugCount = attr.getNamedItem("bugCount").getNodeValue();
				assertThat("UsingOnePlaceHolder.java should have no bug", bugCount, is("0"));
				return;
			}
		}

		fail("Result of pkg/UsingOnePlaceHolder.java not found");
	}

}
