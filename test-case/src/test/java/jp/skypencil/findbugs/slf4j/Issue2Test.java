package jp.skypencil.findbugs.slf4j;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import pkg.Issue2;

public class Issue2Test {

    @Test
    public void test() throws SAXException, IOException, ParserConfigurationException {
        new XmlParser().expectBugs(Issue2.class, 1);
    }

}
