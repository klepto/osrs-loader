package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.analyze.Analyzer;
import dev.klepto.osrs.analyze.FieldInfo;

import java.awt.*;

import static dev.klepto.osrs.OsrsDefinitions.*;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class CanvasImageAnalyzer extends Analyzer {

    @Override
    public boolean analyze(FieldInfo info) {
        if (info.hasDescriptor(Image.class) && !info.isStatic()) {
            mark(CANVAS_BUFFER_PROVIDER_CLASS);
            mark(CANVAS_BUFFER_PROVIDER_IMAGE_FIELD);
            setComplete(true);
            return false;
        }

        return false;
    }

}
