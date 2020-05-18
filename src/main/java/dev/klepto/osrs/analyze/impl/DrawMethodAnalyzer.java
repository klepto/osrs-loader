package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.analyze.Analyzer;
import dev.klepto.osrs.analyze.ClassInfo;
import dev.klepto.osrs.analyze.MethodInfo;

import static dev.klepto.osrs.OsrsDefinitions.*;

public class DrawMethodAnalyzer extends Analyzer {

    @Override
    public boolean analyze(ClassInfo info) {
        return info.equals(CANVAS_BUFFER_PROVIDER_CLASS.getClassInfo());
    }

    @Override
    public boolean analyze(MethodInfo info) {
        if (info.getDescriptor().equals("(Ljava/awt/Graphics;III)V")) {
            mark(CANVAS_BUFFER_PROVIDER_DRAW_METHOD);
            setComplete(true);
            return false;
        }

        return false;
    }

}
