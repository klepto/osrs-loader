package dev.klepto.osrs.analyze;

import static org.objectweb.asm.Opcodes.ACC_STATIC;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public interface AccessInfo {

    int getAccess();

    default boolean isStatic() {
        return (getAccess() & ACC_STATIC) != 0;
    }

}
