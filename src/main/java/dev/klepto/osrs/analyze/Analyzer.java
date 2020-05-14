package dev.klepto.osrs.analyze;

import dev.klepto.osrs.OsrsDefinitions;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Getter
@Setter
public abstract class Analyzer {

    private boolean complete;
    private boolean active;
    private Analyzers analyzers;
    private ClassInfo classInfo;
    private MethodInfo methodInfo;
    private FieldInfo fieldInfo;

    public void mark(OsrsDefinitions definition) {
        definition.setClassInfo(getClassInfo());
        definition.setFieldInfo(getFieldInfo());
        definition.setMethodInfo(getMethodInfo());
        setActive(true);
    }

    public boolean isMarked(OsrsDefinitions definitions) {
        return definitions.getClassInfo() != null
                || definitions.getFieldInfo() != null
                || definitions.getMethodInfo() != null;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
        setActive(true);
    }

    public boolean analyze(ClassInfo info) {
        return true;
    }

    public boolean analyze(FieldInfo info) {
        return false;
    }

    public boolean analyze(MethodInfo info) {
        return false;
    }

    public boolean analyze(FieldInsnInfo info) {
        return false;
    }

}