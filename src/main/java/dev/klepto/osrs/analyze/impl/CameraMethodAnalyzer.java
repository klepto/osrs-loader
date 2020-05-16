package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.analyze.Analyzer;
import dev.klepto.osrs.analyze.InsnInfo;
import dev.klepto.osrs.analyze.MethodInfo;

import java.util.List;

import static dev.klepto.osrs.OsrsDefinitions.CAMERA_CALCULATE_METHOD;
import static org.objectweb.asm.Opcodes.*;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class CameraMethodAnalyzer extends Analyzer {

    @Override
    public boolean analyze(MethodInfo info) {
        return info.getDescriptor().equals("(IIIII)V");
    }

    @Override
    public void analyze(List<InsnInfo> instructions) {
        if (instructions.size() < 3) {
            return;
        }

        if (instructions.get(0).getOpcode() == GETSTATIC
                && instructions.get(3).getOpcode() == PUTSTATIC) {
            mark(CAMERA_CALCULATE_METHOD);
            setComplete(true);
        }
    }
}
