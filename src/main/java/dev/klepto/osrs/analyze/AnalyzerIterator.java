package dev.klepto.osrs.analyze;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.objectweb.asm.Opcodes.LDC;

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
            if (analyzer.analyze(info)) {
                return new MethodIterator();
            }
            return null;
        }
    }

    private class MethodIterator extends MethodVisitor {

        private final List<InsnInfo> instructions = new ArrayList<>();

        public MethodIterator() {
            super(Opcodes.ASM7);
        }


        @Override
        public void visitInsn(int opcode) {
            if (shouldIgnoreRemaining()) {
                return;
            }

            instructions.add(new InsnInfo(instructions.size(), opcode));
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (shouldIgnoreRemaining()) {
                return;
            }

            instructions.add(new IntInsnInfo(instructions.size(), opcode, operand));
        }

        @Override
        public void visitLdcInsn(Object value) {
            if (shouldIgnoreRemaining()) {
                return;
            }

            instructions.add(new LdcInsnInfo(instructions.size(), LDC, value));
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            if (shouldIgnoreRemaining()) {
                return;
            }

            instructions.add(new FieldInsnInfo(instructions.size(), opcode, owner, name, descriptor));
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            if (shouldIgnoreRemaining()) {
                return;
            }

            instructions.add(new TypeInsnInfo(instructions.size(), opcode, type));
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            instructions.add(new InsnInfo(instructions.size(), opcode));
        }

        @Override
        public void visitEnd() {
            analyzer.setInstructions(instructions);
            analyzer.analyze(instructions);
        }
    }

}
