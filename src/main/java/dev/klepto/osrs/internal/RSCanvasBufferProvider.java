package dev.klepto.osrs.internal;

import dev.klepto.osrs.transform.Interface;
import dev.klepto.osrs.transform.Getter;

import javax.swing.*;
import java.awt.*;

import static dev.klepto.osrs.OsrsDefinitions.CANVAS_BUFFER_PROVIDER_CLASS;
import static dev.klepto.osrs.OsrsDefinitions.CANVAS_BUFFER_PROVIDER_IMAGE_FIELD;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@Interface(CANVAS_BUFFER_PROVIDER_CLASS)
public interface RSCanvasBufferProvider extends RSBufferProvider {

    @Getter(CANVAS_BUFFER_PROVIDER_IMAGE_FIELD)
    Image getImage();

    void setCallback(Runnable runnable);

}
