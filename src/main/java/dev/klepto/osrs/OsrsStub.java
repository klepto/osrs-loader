package dev.klepto.osrs;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class OsrsStub implements AppletStub, AppletContext {

    static String nig;

    private final String javConfig;
    private final Properties properties;

    @Override
    public AudioClip getAudioClip(URL url) {
        return null;
    }

    @Override
    public Image getImage(URL url) {
        return null;
    }

    @Override
    public Applet getApplet(String name) {
        return null;
    }

    @Override
    public Enumeration<Applet> getApplets() {
        return null;
    }

    @Override
    public void showDocument(URL url) {

    }

    @Override
    public void showDocument(URL url, String target) {

    }

    @Override
    public void showStatus(String status) {

    }

    @Override
    public void setStream(String key, InputStream stream) throws IOException {

    }

    @Override
    public InputStream getStream(String key) {
        return null;
    }

    @Override
    public Iterator<String> getStreamKeys() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    @SneakyThrows
    public URL getDocumentBase() {
        return new URL((String) properties.get("codebase"));
    }

    @Override
    public URL getCodeBase() {
        return getDocumentBase();
    }

    @Override
    public String getParameter(String name) {
        val paramName = "=" + name + "=";
        val paramIndex = javConfig.indexOf(paramName) + paramName.length();
        val lineEndIndex = javConfig.indexOf('\n', paramIndex);
        if (paramIndex == -1 || lineEndIndex == -1 || name.equals("0")) {
            return null;
        }

        return javConfig.substring(paramIndex, lineEndIndex);
    }

    @Override
    public AppletContext getAppletContext() {
        return this;
    }

    @Override
    public void appletResize(int width, int height) {

    }

}
