package dev.klepto.osrs.internal;

import dev.klepto.osrs.transform.Interface;
import dev.klepto.osrs.transform.Getter;
import dev.klepto.osrs.transform.Setter;

import static dev.klepto.osrs.OsrsDefinitions.*;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Interface(CLIENT_CLASS)
public interface RSClient {

    @Getter(USERNAME_FIELD)
    String getUsername();

    @Getter(PASSWORD_FIELD)
    String getPassword();

    @Getter(BUFFER_PROVIDER_FIELD)
    RSBufferProvider getBufferProvider();

    @Getter(CAMERA_PITCH_FIELD)
    int getCameraPitch();

    @Getter(CAMERA_YAW_FIELD)
    int getCameraYaw();

    @Getter(CAMERA_X_FIELD)
    int getCameraX();

    @Getter(CAMERA_Y_FIELD)
    int getCameraY();

    @Getter(CAMERA_Z_FIELD)
    int getCameraZ();

    @Setter(CAMERA_PITCH_FIELD)
    void setCameraPitch(int pitch);

    @Setter(CAMERA_YAW_FIELD)
    void setCameraYaw(int yaw);

    @Setter(CAMERA_X_FIELD)
    void setCameraX(int x);

    @Setter(CAMERA_Y_FIELD)
    void setCameraY(int y);

    @Setter(CAMERA_Z_FIELD)
    void setCameraZ(int z);

}
