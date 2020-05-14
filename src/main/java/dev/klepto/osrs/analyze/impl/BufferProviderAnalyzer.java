package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.analyze.Analyzer;
import dev.klepto.osrs.analyze.ClassInfo;
import dev.klepto.osrs.analyze.FieldInfo;
import lombok.val;

import static dev.klepto.osrs.OsrsDefinitions.*;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class BufferProviderAnalyzer extends Analyzer {

    private String bufferProvider;

    @Override
    public boolean analyze(ClassInfo info) {
        if (!isMarked(CANVAS_BUFFER_PROVIDER_CLASS)) {
            return false;
        }

        val canvasBufferProvider = CANVAS_BUFFER_PROVIDER_CLASS.getClassInfo().getName();
        if (info.getName().equals(canvasBufferProvider)) {
            bufferProvider = info.getSuperName();
        }

        if (info.getName().equals(bufferProvider)) {
            mark(BUFFER_PROVIDER_CLASS);
        }

        return isMarked(BUFFER_PROVIDER_CLASS);
    }

    @Override
    public boolean analyze(FieldInfo info) {
        val bufferProvider = BUFFER_PROVIDER_CLASS.getClassInfo().getName();
        if (info.hasDescriptor(bufferProvider) && info.isStatic()) {
            mark(BUFFER_PROVIDER_FIELD);
            setComplete(true);
            return true;
        }

        return false;
    }
}
