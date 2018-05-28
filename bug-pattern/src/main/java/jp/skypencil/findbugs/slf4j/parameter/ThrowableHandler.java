package jp.skypencil.findbugs.slf4j.parameter;

import com.google.common.annotations.VisibleForTesting;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.internalAnnotations.StaticConstant;
import javax.annotation.Nonnull;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public class ThrowableHandler {
  @StaticConstant @Nonnull @VisibleForTesting public static final JavaClass THROWABLE;

  static {
    try {
      THROWABLE = Repository.lookupClass(Throwable.class);
    } catch (ClassNotFoundException e) {
      throw new AssertionError("Unreachable");
    }
  }

  /** @return true if given Item is throwable */
  public boolean checkThrowable(Item stackItem) {
    try {
      JavaClass typeOfStack = stackItem.getJavaClass();
      if (typeOfStack == null) {
        // means primitive or array
        return false;
      }
      return typeOfStack.instanceOf(THROWABLE);
    } catch (ClassNotFoundException e) {
      AnalysisContext.reportMissingClass(e);
      return false;
    }
  }
}
