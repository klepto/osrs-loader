package dev.klepto.osrs.analyze.impl;

import dev.klepto.osrs.analyze.*;
import lombok.val;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static dev.klepto.osrs.OsrsDefinitions.*;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Type.getDescriptor;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class AccountFieldAnalyzer extends Analyzer {

    @Override
    public boolean analyze(MethodInfo info) {
        return info.equals(LOGIN_METHOD.getMethodInfo());
    }

    @Override
    public void analyze(List<InsnInfo> instructions) {
        val startIndex = getInstructions(FieldInsnInfo.class).stream()
                .filter(info -> info.hasDescriptor(LinkedHashMap.class))
                .map(InsnInfo::getIndex)
                .findFirst().orElse(0);

        val stringFields = getInstructions().stream().skip(startIndex)
                .filter(FieldInsnInfo.class::isInstance)
                .map(FieldInsnInfo.class::cast)
                .filter(info -> info.hasDescriptor(String.class))
                .collect(Collectors.toList());

        val username = stringFields.get(0);
        val password = stringFields.get(1);
        if (!isFieldDefined(username) || !isFieldDefined(password)) {
            return;
        }
        mark(USERNAME_FIELD, username);
        mark(PASSWORD_FIELD, password);
        setComplete(true);
    }

}
