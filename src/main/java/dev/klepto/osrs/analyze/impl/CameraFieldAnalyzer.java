package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.analyze.Analyzer;
import dev.klepto.osrs.analyze.FieldInsnInfo;
import dev.klepto.osrs.analyze.InsnInfo;
import dev.klepto.osrs.analyze.MethodInfo;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

import static dev.klepto.osrs.OsrsDefinitions.*;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFNE;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class CameraFieldAnalyzer extends Analyzer {

    @Override
    public boolean analyze(MethodInfo info) {
        return info.equals(CAMERA_CALCULATE_METHOD.getMethodInfo());
    }

    @Override
    public void analyze(List<InsnInfo> instructions) {
        var startIndex = 0;
        for (int i = 0; i < instructions.size() - 1; i++) {
            if (instructions.get(i).getOpcode() == GETSTATIC
                && instructions.get(i + 1).getOpcode() == IFNE) {
                startIndex = i + 1;
                break;
            }
        }

        val fieldInstructions = instructions.stream()
                .skip(startIndex)
                .filter(FieldInsnInfo.class::isInstance)
                .map(FieldInsnInfo.class::cast)
                .collect(Collectors.toList());

        val pitch = fieldInstructions.get(0);
        val yaw = fieldInstructions.get(16);
        val x = fieldInstructions.get(5);
        val y = fieldInstructions.get(6);
        val z = fieldInstructions.get(3);

        mark(CAMERA_PITCH_FIELD, pitch);
        mark(CAMERA_YAW_FIELD, yaw);
        mark(CAMERA_X_FIELD, x);
        mark(CAMERA_Y_FIELD, y);
        mark(CAMERA_Z_FIELD, z);
        setComplete(true);
    }

}
