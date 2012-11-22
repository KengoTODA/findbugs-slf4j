package jp.skypencil.findbugs.slf4j;

import org.apache.bcel.classfile.Field;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class NonFinalLoggerDetector extends OpcodeStackDetector {
    private final BugReporter bugReporter;

    public NonFinalLoggerDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visit(Field field) {
        if (!field.isFinal() && field.getSignature().equals("Lorg/slf4j/Logger;")) {
            BugInstance bug = new BugInstance(this,
                    "SLF4J_LOGGER_SHOULD_BE_FINAL", NORMAL_PRIORITY)
                    .addString(field.getName())
                    .addField(this)
                    .addClass(this);
            bugReporter.reportBug(bug);
        }
    }

    @Override
    public void sawOpcode(int code) {
    }
}
