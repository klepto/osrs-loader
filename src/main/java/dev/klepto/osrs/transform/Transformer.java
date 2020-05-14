package dev.klepto.osrs.transform;

import com.google.common.collect.ObjectArrays;
import dev.klepto.osrs.OsrsDefinitions;
import dev.klepto.osrs.OsrsLoader;
import dev.klepto.osrs.analyze.ClassBytes;
import io.github.classgraph.ClassGraph;
import lombok.val;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassReader.SKIP_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

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
                    .filter(info -> info.hasAnnotation(TargetClass.class.getName()))
                    .loadClasses());
        }
    }

    public ClassBytes transform(ClassBytes classBytes) {
        val tranformations = this.transformations.stream()
                .filter(inter -> {
                    val annotation = inter.getAnnotation(TargetClass.class).value();
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
        val classWriter = new ClassWriter(classReader, SKIP_FRAMES);
        val visitor = new ClassVisitor(ASM7, classWriter) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                interfaces = ObjectArrays.concat(interfaces, transformation.getName().replace('.', '/'));
                super.visit(version, access, name, signature, superName, interfaces);
                for (val method : methods) {
                    createTransformMethod(this, method);
                }
            }
        };

        classReader.accept(visitor, EXPAND_FRAMES);
        return new ClassBytes(classBytes.getName(), classWriter.toByteArray());
    }

    private void createTransformMethod(ClassVisitor classVisitor, Method method) {
        if (method.isAnnotationPresent(TargetGetter.class)) {
            createGetter(classVisitor, method, method.getAnnotation(TargetGetter.class).value());
        }
    }

    private void createGetter(ClassVisitor classVisitor, Method method, OsrsDefinitions definition) {
        System.out.println("Creating: " + method);
        val type = Type.getType(method);
        val visitor = classVisitor.visitMethod(ACC_PUBLIC, method.getName(), type.getDescriptor(), null, null);
        val classInfo = definition.getClassInfo();
        val info = definition.getFieldInfo();

        if (!info.isStatic()) {
            visitor.visitVarInsn(ALOAD, 0);
            visitor.visitFieldInsn(GETFIELD, classInfo.getName(), info.getName(), info.getDescriptor());
        } else {
            visitor.visitFieldInsn(GETSTATIC, classInfo.getName(), info.getName(), info.getDescriptor());
        }
        visitor.visitMaxs(2, 2);
        visitor.visitInsn(ARETURN);
        visitor.visitEnd();
    }

}
