package dev.klepto.osrs.analyze;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LdcInsnInfo extends InsnInfo {

    private int index;
    private int opcode;
    private Object value;

}
