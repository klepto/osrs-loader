package dev.klepto.osrs.api;

import dev.klepto.osrs.transform.TargetClass;
import dev.klepto.osrs.transform.TargetGetter;

import java.awt.*;

import static dev.klepto.osrs.OsrsDefinitions.CANVAS_BUFFER_PROVIDER_CLASS;
import static dev.klepto.osrs.OsrsDefinitions.CANVAS_BUFFER_PROVIDER_IMAGE_FIELD;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@TargetClass(CANVAS_BUFFER_PROVIDER_CLASS)
public interface RSCanvasBufferProvider extends RSBufferProvider {

    @TargetGetter(CANVAS_BUFFER_PROVIDER_IMAGE_FIELD)
    Image getImage();

}
