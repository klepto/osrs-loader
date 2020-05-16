package dev.klepto.osrs.analyze;

import dev.klepto.osrs.OsrsDefinitions;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.objectweb.asm.Type.getType;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Getter
@Setter
public abstract class Analyzer {

    private final Map<String, ClassInfo> classes = new HashMap<>();
    private final Map<String, FieldInfo> fields = new HashMap<>();

    private boolean complete;
    private boolean active;
    private Analyzers analyzers;
    private ClassInfo classInfo;
    private MethodInfo methodInfo;
    private FieldInfo fieldInfo;
    private List<InsnInfo> instructions;

    public void setClassInfo(ClassInfo info) {
        classes.put(info.getName(), info);
        classInfo = info;
    }

    public void setFieldInfo(FieldInfo info) {
        fields.put(getClassInfo().getName() + "." + info.getName(), info);
        fieldInfo = info;
    }

    public void mark(OsrsDefinitions definition) {
        definition.setClassInfo(getClassInfo());
        definition.setFieldInfo(getFieldInfo());
        definition.setMethodInfo(getMethodInfo());
        setActive(true);
    }

    public void mark(OsrsDefinitions definition, FieldInsnInfo info) {
        val classInfo = classes.get(info.getOwner());
        val fieldInfo = fields.get(info.getOwner() + "." + info.getName());
        definition.setClassInfo(classInfo);
        definition.setFieldInfo(fieldInfo);
        if (getType(info.getDescriptor()).getSort() == Type.INT) {
            definition.setGetMultiplier(findMultiplier(info));
        }
        setActive(true);
    }

    public boolean isFieldDefined(FieldInsnInfo info) {
        return fields.containsKey(info.getOwner() + "." + info.getName());
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

    public int findMultiplier(InsnInfo info) {
        return findMultiplier(info.getIndex());
    }

    public int findMultiplier(int index) {
        val previous = index > 0 ? instructions.get(index - 1) :  instructions.get(index);
        val next = index < instructions.size() - 1 ? instructions.get(index + 1) : instructions.get(index);
        val ldc = (LdcInsnInfo) (previous instanceof LdcInsnInfo ? previous : next instanceof LdcInsnInfo ? next : null);
        if (ldc == null) {
            return 0;
        }

        return (int) ldc.getValue();
    }

    public <T extends InsnInfo> List<T> getInstructions(Class<T> type) {
        return getInstructions().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
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

    public void analyze(List<InsnInfo> instructions) {

    }

}