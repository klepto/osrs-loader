package dev.klepto.osrs.analyze;

import lombok.*;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FieldInsnInfo extends InsnInfo implements DescriptorInfo {

    private int index;
    private int opcode;
    private String owner;
    private String name;
    private String descriptor;

}
