package dev.klepto.osrs.analyze.impl;
import dev.klepto.osrs.analyze.Analyzer;
import dev.klepto.osrs.analyze.ClassInfo;

import static dev.klepto.osrs.OsrsDefinitions.CLIENT_CLASS;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class ClientAnalyzer extends Analyzer {

    @Override
    public boolean analyze(ClassInfo info) {
        if (info.getName().equals("client")) {
            mark(CLIENT_CLASS);
            setComplete(true);
        }

        return true;
    }

}
