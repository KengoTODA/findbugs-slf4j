package jp.skypencil.findbugs.slf4j.sonar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

/**
 * <p>Generate rule.xml for Sonar (SonarQube).</p>
 */
public class RuleXmlGenerator {
    private static final Joiner PATH_JOINER = Joiner.on(File.separatorChar);

    public static void main(String[] args) throws IOException {
        String projectRoot = "..";
        if (new File("sonar-plugin").exists()) {
            projectRoot = ".";
        }
        File metaDirectory = new File(PATH_JOINER.join(projectRoot,
                "bug-pattern", "src", "main", "resources"));
        File findbugsFile = new File(metaDirectory, "findbugs.xml");
        File messageFile = new File(metaDirectory, "messages.xml");
        File output = new File(PATH_JOINER.join(projectRoot, "sonar-plugin", "src", "main", "resources", "jp", "skypencil", "findbugs", "slf4j", "sonar"), "rules.xml");
        new RuleXmlGenerator().generate(findbugsFile, messageFile, output);
    }

    private static final Set<String> BLOCKER = Sets.newHashSet(
            "SLF4J_FORMAT_SHOULD_BE_CONST", // because this bug hides SLF4J_PLACE_HOLDER_MISMATCH and others
            "SLF4J_PLACE_HOLDER_MISMATCH");
    private static final Set<String> CRITICAL = Sets.newHashSet(
            "SLF4J_UNKNOWN_ARRAY",
            "SLF4J_ILLEGAL_PASSED_CLASS");
    private static final Set<String> MAJOR = Sets.newHashSet(
            "SLF4J_SIGN_ONLY_FORMAT",
            "SLF4J_LOGGER_SHOULD_BE_PRIVATE",
            "SLF4J_LOGGER_SHOULD_BE_NON_STATIC",
            "SLF4J_LOGGER_SHOULD_BE_FINAL",
            "SLF4J_MANUALLY_PROVIDED_MESSAGE");

    void generate(File findbugsFile, File messageFile, File output) throws IOException {
        SAXReader reader = new SAXReader();
        try {
            Document message = reader.read(messageFile);
            Document findbugs = reader.read(findbugsFile);

            @SuppressWarnings("unchecked")
            List<Node> bugPatterns = message.selectNodes("/MessageCollection/BugPattern");
            @SuppressWarnings("unchecked")
            List<Node> findbugsAbstract = findbugs.selectNodes("/FindbugsPlugin/BugPattern");

            writePatterns(findbugsAbstract, bugPatterns, output);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private void writePatterns(List<Node> findbugsAbstract, List<Node> bugPatterns, File output) throws IOException {
        BufferedWriter writer = Files.newWriter(output, Charsets.UTF_8);
        boolean threw = true;
        try {
            writer.write("<rules>\n");
            for (Node bugPattern : bugPatterns) {
                writeBugPattern(bugPattern, findbugsAbstract, writer);
            }
            writer.write("</rules>\n");
            threw = false;
        } finally {
            Closeables.close(writer, threw);
        }
    }

    private void writeBugPattern(Node bugPattern, List<Node> findbugsAbstract, BufferedWriter writer) throws IOException {
        String type = bugPattern.valueOf("@type");
        String description = bugPattern.selectSingleNode("ShortDescription").getText();

        Node data = findByType(findbugsAbstract, type);
        String abbrev = data.valueOf("@abbrev");
        String category = data.valueOf("@category");
        String priority = decidePriority(type);

        String line = String.format("  <rule key=\"%s\">\n" +
                "    <priority>%s</priority>\n" +
                "    <name><![CDATA[%s - %s]]></name>\n" +
                "    <description><![CDATA[[%s] %s]]></description>\n" +
                "    <configKey><![CDATA[%s]]></configKey>\n" +
                "  </rule>\n",
                type, priority, category, description, abbrev, description, type);
        writer.write(line);
    }

    private String decidePriority(String type) {
        if (BLOCKER.contains(type)) {
            return "BLOCKER";
        } else if (CRITICAL.contains(type)) {
            return "CRITICAL";
        } else if (MAJOR.contains(type)) {
            return "MAJOR";
        }
        throw new IllegalArgumentException("priority for " + type + " is not defined");
    }

    private Node findByType(List<Node> findbugsAbstract, String type) {
        for (Node node : findbugsAbstract) {
            if (Objects.equal(type, node.valueOf("@type"))) {
                return node;
            }
        }
        throw new IllegalArgumentException("cannot find " + type);
    }

}
