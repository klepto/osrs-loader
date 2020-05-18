package dev.klepto.osrs.transform;

import com.google.common.collect.ObjectArrays;
import dev.klepto.osrs.OsrsDefinitions;
import dev.klepto.osrs.OsrsLoader;
import dev.klepto.osrs.analyze.ClassBytes;
import dev.klepto.osrs.internal.RSCanvasBufferProvider;
import io.github.classgraph.ClassGraph;
import lombok.val;
import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.klepto.osrs.OsrsDefinitions.CANVAS_BUFFER_PROVIDER_DRAW_METHOD;
import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;
import static org.objectweb.asm.Type.DOUBLE;
import static org.objectweb.asm.Type.FLOAT;
import static org.objectweb.asm.Type.LONG;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class Transformer {

    private final Set<Class<?>> transformations;

    public Transformer() {
        this.transformations = resolveTranformations();
    }

    private Set<Class<?>> resolveTranformations() {
        try (val result = new ClassGraph()
                .whitelistPackages(OsrsLoader.class.getPackageName())
                .enableAnnotationInfo()
                .scan()) {
            return new HashSet<>(result.getAllClasses()
                    .filter(info -> info.hasAnnotation(Interface.class.getName()))
                    .loadClasses());
        }
    }

    public ClassBytes transform(ClassBytes classBytes) {
        val tranformations = this.transformations.stream()
                .filter(inter -> {
                    val annotation = inter.getAnnotation(Interface.class).value();
                    val className = annotation.getClassInfo().getName();
                    return classBytes.getName().equals(className);
                }).collect(Collectors.toSet());

        var result = classBytes;
        for (val transformation : tranformations) {
            result = transform(result, transformation);
        }
        return result;
    }

    private ClassBytes transform(ClassBytes classBytes, Class<?> transformation) {
        val methods = transformation.getDeclaredMethods();
        val classReader = new ClassReader(classBytes.getBytecode());
        val classWriter = new ClassWriter(classReader, COMPUTE_FRAMES);
        val visitor = new ClassVisitor(ASM7, classWriter) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                interfaces = ObjectArrays.concat(interfaces, transformation.getName().replace('.', '/'));
                super.visit(version, access, name, signature, superName, interfaces);
                for (val method : methods) {
                    createTransformMethod(this, method);
                }
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (transformation == RSCanvasBufferProvider.class
                        && name.equals(CANVAS_BUFFER_PROVIDER_DRAW_METHOD.getMethodInfo().getName())) {
                    createRenderingCallback(this, methodVisitor);
                    return null;
                }
                return methodVisitor;
            }
        };

        classReader.accept(visitor, EXPAND_FRAMES);
        return new ClassBytes(classBytes.getName(), classWriter.toByteArray());
    }

    private void createRenderingCallback(ClassVisitor classVisitor, MethodVisitor visitor) {
        val classInfo = CANVAS_BUFFER_PROVIDER_DRAW_METHOD.getClassInfo();
        val methodInfo = CANVAS_BUFFER_PROVIDER_DRAW_METHOD.getMethodInfo();

        visitor.visitFieldInsn(GETSTATIC, classInfo.getName(), "callback", "Ljava/lang/Runnable;");
        val label = new Label();
        visitor.visitJumpInsn(IFNULL, label);
        visitor.visitFieldInsn(GETSTATIC, classInfo.getName(), "callback", "Ljava/lang/Runnable;");
        visitor.visitMethodInsn(INVOKEINTERFACE, "java/lang/Runnable", "run", "()V", true);
        visitor.visitLabel(label);
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(1, 2);
        visitor.visitEnd();

        val fieldVisitor = classVisitor.visitField(ACC_STATIC, "callback", "Ljava/lang/Runnable;", null, null);
        fieldVisitor.visitEnd();

        visitor = classVisitor.visitMethod(ACC_PUBLIC, "setCallback", "(Ljava/lang/Runnable;)V", null, null);
        visitor.visitVarInsn(ALOAD, 1);
        visitor.visitFieldInsn(PUTSTATIC, classInfo.getName(), "callback", "Ljava/lang/Runnable;");
        visitor.visitInsn(RETURN);
        visitor.visitMaxs(1, 2);
        visitor.visitEnd();
    }

    private void createTransformMethod(ClassVisitor classVisitor, Method method) {
        if (method.isAnnotationPresent(Getter.class)) {
            createGetter(classVisitor, method, method.getAnnotation(Getter.class).value());
        }
        if (method.isAnnotationPresent(Setter.class)) {
            createSetter(classVisitor, method, method.getAnnotation(Setter.class).value());
        }
    }

    private void createGetter(ClassVisitor classVisitor, Method method, OsrsDefinitions definition) {
        val type = getType(method);
        val visitor = classVisitor.visitMethod(ACC_PUBLIC, method.getName(), type.getDescriptor(), null, null);
        val classInfo = definition.getClassInfo();
        val info = definition.getFieldInfo();

        if (!info.isStatic()) {
            visitor.visitVarInsn(ALOAD, 0);
            visitor.visitFieldInsn(GETFIELD, classInfo.getName(), info.getName(), info.getDescriptor());
        } else {
            visitor.visitFieldInsn(GETSTATIC, classInfo.getName(), info.getName(), info.getDescriptor());
        }
        if (definition.getGetMultiplier() != 0 && info.hasDescriptor(int.class)) {
            visitor.visitLdcInsn(definition.getGetMultiplier());
            visitor.visitInsn(IMUL);
        }
        visitor.visitMaxs(3, 3);
        appendReturn(visitor, method.getReturnType());
        visitor.visitEnd();
    }

    private void createSetter(ClassVisitor classVisitor, Method method, OsrsDefinitions definition) {
        val type = getType(method);
        val visitor = classVisitor.visitMethod(ACC_PUBLIC, method.getName(), type.getDescriptor(), null, null);
        val classInfo = definition.getClassInfo();
        val info = definition.getFieldInfo();

        if (!info.isStatic()) {
            visitor.visitVarInsn(ALOAD, 0);
        }

        appendLoad(visitor, method.getParameterTypes()[0], 1);
        if (definition.getSetMultiplier() != 0) {
            visitor.visitLdcInsn(definition.getSetMultiplier());
            visitor.visitInsn(IMUL);
        }

        if (!info.isStatic()) {
            visitor.visitFieldInsn(PUTFIELD, classInfo.getName(), info.getName(), info.getDescriptor());
        } else {
            visitor.visitFieldInsn(PUTSTATIC, classInfo.getName(), info.getName(), info.getDescriptor());
        }

        visitor.visitMaxs(2, 2);
        visitor.visitInsn(RETURN);
        visitor.visitEnd();
    }

    private void appendLoad(MethodVisitor methodVisitor, Class<?> classType, int index) {
        val type = getType(classType);
        switch (type.getSort()) {
            case ARRAY:
            case OBJECT:
                methodVisitor.visitIntInsn(ALOAD, index);
                break;
            case BOOLEAN:
            case CHAR:
            case SHORT:
            case INT:
                methodVisitor.visitIntInsn(ILOAD, index);
                break;
            case LONG:
                methodVisitor.visitIntInsn(LLOAD, index);
                break;
            case FLOAT:
                methodVisitor.visitIntInsn(FLOAD, index);
                break;
            case DOUBLE:
                methodVisitor.visitIntInsn(DLOAD, index);
                break;
        }
    }

    private void appendReturn(MethodVisitor methodVisitor, Class<?> classType) {
        val type = getType(classType);
        switch (type.getSort()) {
            case ARRAY:
            case OBJECT:
                methodVisitor.visitInsn(ARETURN);
                break;
            case BOOLEAN:
            case CHAR:
            case SHORT:
            case INT:
                methodVisitor.visitInsn(IRETURN);
                break;
            case LONG:
                methodVisitor.visitInsn(LRETURN);
                break;
            case FLOAT:
                methodVisitor.visitInsn(FRETURN);
                break;
            case DOUBLE:
                methodVisitor.visitInsn(DRETURN);
                break;
        }
    }

}
