package dev.klepto.osrs;

import dev.klepto.osrs.analyze.ClassInfo;
import dev.klepto.osrs.analyze.FieldInfo;
import dev.klepto.osrs.analyze.MethodInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public enum OsrsDefinitions {

    CLIENT_CLASS,
    BUFFER_PROVIDER_CLASS,
    BUFFER_PROVIDER_FIELD,
    CANVAS_BUFFER_PROVIDER_CLASS,
    CANVAS_BUFFER_PROVIDER_IMAGE_FIELD,
    CAMERA_CALCULATE_METHOD,
    CAMERA_PITCH_FIELD,
    CAMERA_YAW_FIELD,
    CAMERA_X_FIELD,
    CAMERA_Y_FIELD,
    CAMERA_Z_FIELD,
    LOGIN_METHOD,
    USERNAME_FIELD,
    PASSWORD_FIELD;

    @Setter private int getMultiplier;
    @Setter private int setMultiplier;
    @Getter @Setter private ClassInfo classInfo;
    @Getter @Setter private FieldInfo fieldInfo;
    @Getter @Setter private MethodInfo methodInfo;

    public int getGetMultiplier() {
        if (getMultiplier == 0 && setMultiplier != 0) {
            setGetMultiplier(BigInteger.valueOf(setMultiplier).modInverse(BigInteger.ONE.shiftLeft(32)).intValue());
        }
        return getMultiplier;
    }

    public int getSetMultiplier() {
        if (setMultiplier == 0 && getMultiplier != 0) {
            setSetMultiplier(BigInteger.valueOf(getMultiplier).modInverse(BigInteger.ONE.shiftLeft(32)).intValue());
        }
        return setMultiplier;
    }

}
