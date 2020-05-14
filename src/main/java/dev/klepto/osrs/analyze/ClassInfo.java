package dev.klepto.osrs.analyze;

import lombok.Value;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Value
public class ClassInfo implements AccessInfo {

    int version;
    int access;
    String name;
    String signature;
    String superName;
    String[] interfaces;

    public boolean hasSuper(Class<?> type) {
        return hasSuper(type.getName());
    }

    public boolean hasSuper(String className) {
        return className.replace(".", "/").equals(superName);
    }

}
