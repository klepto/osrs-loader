package dev.klepto.osrs.analyze;

import lombok.Value;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Value
public class FieldInfo implements AccessInfo, DescriptorInfo {

    int access;
    String name;
    String descriptor;
    String signature;
    Object value;

}
