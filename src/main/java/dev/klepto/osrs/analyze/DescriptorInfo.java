package dev.klepto.osrs.analyze;

import org.objectweb.asm.Type;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public interface DescriptorInfo {

    String getDescriptor();

    default boolean hasDescriptor(Class<?> type) {
        return getDescriptor().equals(Type.getDescriptor(type));
    }

    default boolean hasDescriptor(String className) {
        return getDescriptor().equals("L" + className.replace(".", "/") + ";");
    }

}
