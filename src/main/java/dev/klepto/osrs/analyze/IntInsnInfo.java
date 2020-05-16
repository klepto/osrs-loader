package dev.klepto.osrs.analyze;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntInsnInfo extends InsnInfo {

    private int index;
    private int opcode;
    private int operand;

}
