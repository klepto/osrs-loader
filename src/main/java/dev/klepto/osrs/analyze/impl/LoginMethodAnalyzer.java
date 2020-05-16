package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.OsrsDefinitions;
import dev.klepto.osrs.analyze.*;
import lombok.val;

import java.net.Socket;
import java.util.List;

import static dev.klepto.osrs.OsrsDefinitions.LOGIN_METHOD;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Type.getInternalName;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class LoginMethodAnalyzer extends Analyzer {

    @Override
    public boolean analyze(MethodInfo info) {
        return info.getDescriptor().equals("(I)V");
    }

    @Override
    public void analyze(List<InsnInfo> instructions) {
        val containsSocket = getInstructions(TypeInsnInfo.class).stream()
                .anyMatch(info -> info.getType().equals(getInternalName(Socket.class)));
        val containsBitSize = getInstructions(IntInsnInfo.class).stream()
                .anyMatch(info -> info.getOpcode() == SIPUSH && info.getOperand() == 500);
        val constainBufferSizes = getInstructions(LdcInsnInfo.class).stream()
                .anyMatch(info -> info.getValue().equals(40000));

        if (constainBufferSizes && containsSocket && containsBitSize) {
            mark(LOGIN_METHOD);
            setComplete(true);
        }
    }

}
