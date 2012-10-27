package jp.skypencil.findbugs.slf4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EnsureNoErrorTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse("target/findbugsXml.xml");
		NodeList errorsList = doc.getElementsByTagName("Errors");
		Node errorsNode = errorsList.item(0);
		String errorCount = errorsNode.getAttributes().getNamedItem("errors").getNodeValue();

		assertThat(errorCount, is("0"));
	}

}