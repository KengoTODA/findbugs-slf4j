package jp.skypencil.findbugs.slf4j;
import static org.apache.bcel.Repository.lookupClass;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.Type;

final class JavaType {
    private final String name;  // "java.lang.String" etc.
    private final JavaClass javaClass;

    static JavaType from(JavaClass clazz) {
        if (clazz == null) {
            return null;
        } else {
            return new JavaType(clazz);
        }
    }

    JavaType(Type type) {
        this.name = type.toString();
        this.javaClass = null;
    }

    JavaType(JavaClass clazz) {
        this.name = clazz.getClassName();
        this.javaClass = clazz;
    }

    @Override
    public String toString() {
        return name;
    }

    JavaClass toJavaClass() throws ClassNotFoundException {
        if (javaClass != null) {
            return javaClass;
        } else if (name.contains("[")){
            return null;
        } else {
            return lookupClass(name);
        }
    }
}