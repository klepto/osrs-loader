package dev.klepto.osrs.api;

import dev.klepto.osrs.transform.TargetClass;
import dev.klepto.osrs.transform.TargetGetter;
import dev.klepto.osrs.transform.TargetSetter;

import static dev.klepto.osrs.OsrsDefinitions.*;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@TargetClass(CLIENT_CLASS)
public interface RSClient {

    @TargetGetter(USERNAME_FIELD)
    String getUsername();

    @TargetGetter(PASSWORD_FIELD)
    String getPassword();

    @TargetGetter(BUFFER_PROVIDER_FIELD)
    RSBufferProvider getBufferProvider();

    @TargetGetter(CAMERA_PITCH_FIELD)
    int getCameraPitch();

    @TargetGetter(CAMERA_YAW_FIELD)
    int getCameraYaw();

    @TargetGetter(CAMERA_X_FIELD)
    int getCameraX();

    @TargetGetter(CAMERA_Y_FIELD)
    int getCameraY();

    @TargetGetter(CAMERA_Z_FIELD)
    int getCameraZ();

    @TargetSetter(CAMERA_PITCH_FIELD)
    void setCameraPitch(int pitch);

    @TargetSetter(CAMERA_YAW_FIELD)
    void setCameraYaw(int yaw);

    @TargetSetter(CAMERA_X_FIELD)
    void setCameraX(int x);

    @TargetSetter(CAMERA_Y_FIELD)
    void setCameraY(int y);

    @TargetSetter(CAMERA_Z_FIELD)
    void setCameraZ(int z);

}
