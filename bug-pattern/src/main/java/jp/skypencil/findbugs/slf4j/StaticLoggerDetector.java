package jp.skypencil.findbugs.slf4j;

import com.google.common.base.Objects;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.classfile.Field;

public class StaticLoggerDetector extends OpcodeStackDetector {
  private final BugReporter bugReporter;

  public StaticLoggerDetector(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
  }

  @Override
  public void visit(Field field) {
    if (field.isStatic() && Objects.equal(field.getSignature(), "Lorg/slf4j/Logger;")) {
      BugInstance bug =
          new BugInstance(this, "SLF4J_LOGGER_SHOULD_BE_NON_STATIC", LOW_PRIORITY)
              .addString(field.getName())
              .addField(this)
              .addClass(this);
      bugReporter.reportBug(bug);
    }
  }

  @Override
  public void sawOpcode(int code) {}
}
