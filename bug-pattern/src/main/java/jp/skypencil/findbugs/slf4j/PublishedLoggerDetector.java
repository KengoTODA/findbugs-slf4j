package jp.skypencil.findbugs.slf4j;

import com.google.common.base.Objects;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.classfile.Field;

public class PublishedLoggerDetector extends OpcodeStackDetector {
  private final BugReporter bugReporter;

  public PublishedLoggerDetector(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
  }

  @Override
  public void visit(Field field) {
    if (!field.isPrivate() && Objects.equal(field.getSignature(), "Lorg/slf4j/Logger;")) {
      BugInstance bug =
          new BugInstance(this, "SLF4J_LOGGER_SHOULD_BE_PRIVATE", NORMAL_PRIORITY)
              .addString(field.getName())
              .addField(this)
              .addClass(this);
      bugReporter.reportBug(bug);
    }
  }

  @Override
  public void sawOpcode(int code) {}
}
