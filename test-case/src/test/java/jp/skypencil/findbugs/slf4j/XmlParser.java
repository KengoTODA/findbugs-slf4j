package jp.skypencil.findbugs.slf4j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XmlParser {
  Multimap<String, String> expect(Class<?> clazz, Map<String, Integer> expected) {
    final String filePath = "pkg." + clazz.getSimpleName();
    Multimap<String, String> longMessages = HashMultimap.create();
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse("target/spotbugsXml.xml");
      NodeList bugs = doc.getElementsByTagName("BugInstance");
      Map<String, Integer> actual = new HashMap<String, Integer>();

      for (int i = 0; i < bugs.getLength(); ++i) {
        Node bug = bugs.item(i);
        NamedNodeMap attr = bug.getAttributes();
        String className = findClass(bug).getNamedItem("classname").getNodeValue();
        if (className.equals(filePath)) {
          String bugType = attr.getNamedItem("type").getNodeValue();
          Integer counter = actual.get(bugType);
          if (counter == null) {
            actual.put(bugType, 1);
          } else {
            actual.put(bugType, counter + 1);
          }
          NodeList children = bug.getChildNodes();
          for (int j = 0; j < children.getLength(); ++j) {
            Node child = children.item(j);
            if (!child.getNodeName().equals("LongMessage")) {
              continue;
            }
            longMessages.put(bugType, child.getTextContent().trim());
          }
        }
      }
      assertThat(actual).isEqualTo(expected);
    } catch (ParserConfigurationException e) {
      throw new AssertionError(e);
    } catch (SAXException e) {
      throw new AssertionError(e);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return longMessages;
  }

  private NamedNodeMap findClass(Node bug) {
    NodeList children = bug.getChildNodes();
    for (int i = 0; i < children.getLength(); ++i) {
      Node child = children.item(i);
      if (child.getNodeName().equals("Class")) {
        return child.getAttributes();
      }
    }
    throw new AssertionError();
  }
}
