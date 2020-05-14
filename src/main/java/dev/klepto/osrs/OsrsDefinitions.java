package dev.klepto.osrs;

import dev.klepto.osrs.analyze.ClassInfo;
import dev.klepto.osrs.analyze.FieldInfo;
import dev.klepto.osrs.analyze.MethodInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public enum OsrsDefinitions {

    CLIENT_CLASS,
    BUFFER_PROVIDER_CLASS,
    BUFFER_PROVIDER_FIELD,
    CANVAS_BUFFER_PROVIDER_CLASS,
    CANVAS_BUFFER_PROVIDER_IMAGE_FIELD;

    @Getter @Setter private ClassInfo classInfo;
    @Getter @Setter private FieldInfo fieldInfo;
    @Getter @Setter private MethodInfo methodInfo;

}
