package dev.klepto.osrs.api;

import dev.klepto.osrs.transform.TargetClass;
import dev.klepto.osrs.transform.TargetGetter;

import static dev.klepto.osrs.OsrsDefinitions.BUFFER_PROVIDER_FIELD;
import static dev.klepto.osrs.OsrsDefinitions.CLIENT_CLASS;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@TargetClass(CLIENT_CLASS)
public interface RSClient {

    @TargetGetter(BUFFER_PROVIDER_FIELD)
    RSBufferProvider getBufferProvider();

}
