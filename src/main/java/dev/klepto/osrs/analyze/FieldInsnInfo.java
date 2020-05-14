package dev.klepto.osrs.analyze;

import lombok.Value;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Value
public class FieldInsnInfo {

    int opcode;
    String owner;
    String name;
    String descriptor;

}
