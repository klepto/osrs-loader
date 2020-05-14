package dev.klepto.osrs.analyze;

import lombok.Value;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Value
public class ClassBytes {

    String name;
    byte[] bytecode;

}
