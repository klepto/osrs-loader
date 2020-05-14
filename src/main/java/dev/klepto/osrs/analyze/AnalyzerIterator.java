package dev.klepto.osrs.analyze;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.objectweb.asm.*;

import java.util.Set;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class AnalyzerIterator {

    private final Analyzer analyzer;
    private final Set<ClassBytes> classBytes;

    private boolean ignoreRemaining;

    public boolean iterate() {
        if (analyzer.isComplete()) {
            return false;
        }

        classBytes.forEach(cls -> {
            ignoreRemaining = false;
            val classReader = new ClassReader(cls.getBytecode());
            classReader.accept(new ClassIterator(), ClassReader.SKIP_FRAMES);
        });

        return analyzer.isActive();
    }

    public boolean shouldIgnoreRemaining() {
        return ignoreRemaining || analyzer.isComplete();
    }

    private class ClassIterator extends ClassVisitor {

        public ClassIterator() {
            super(Opcodes.ASM7);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            val info = new ClassInfo(version, access, name, signature, superName, interfaces);
            analyzer.setClassInfo(info);
            if (!analyzer.analyze(info)) {
                ignoreRemaining = true;
            }
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            if (shouldIgnoreRemaining()) {
                return null;
            }
            val info = new FieldInfo(access, name, descriptor, signature, value);
            analyzer.setFieldInfo(info);
            analyzer.analyze(info);
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (shouldIgnoreRemaining()) {
                return null;
            }
            val info = new MethodInfo(access, name, descriptor, signature, exceptions);
            analyzer.setMethodInfo(info);
            analyzer.analyze(info);
            return null;
        }
    }

    private class MethodIterator extends MethodVisitor {

        public MethodIterator() {
            super(Opcodes.ASM7);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            if (shouldIgnoreRemaining()) {
                return;
            }

            val info = new FieldInsnInfo(opcode, owner, name, descriptor);
            analyzer.analyze(info);
        }

    }

}
