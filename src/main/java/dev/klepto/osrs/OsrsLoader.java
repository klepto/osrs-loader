package dev.klepto.osrs;

import com.google.common.io.ByteStreams;
import dev.klepto.osrs.analyze.Analyzers;
import dev.klepto.osrs.api.RSClient;
import dev.klepto.osrs.transform.Transformer;
import lombok.val;

import javax.swing.*;
import java.applet.Applet;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class OsrsLoader {

    public static void main(String[] args) throws Exception {
        var javConfig = (String) null;
        val properties = new Properties();
        try (val inputStream = new URL("http://oldschool.runescape.com/jav_config.ws").openStream()) {
            val response = ByteStreams.toByteArray(inputStream);
            javConfig = new String(response);
            properties.load(new ByteArrayInputStream(response));
        }

        val codebase = (String) properties.get("codebase");
        val gamepack = (String) properties.get("initial_jar");
        val mainClass = ((String) properties.get("initial_class")).replace(".class", "");
        var classLoader = (OsrsClassLoader) null;
        try (val inputStream = new URL(codebase + gamepack).openStream()) {
            classLoader = new OsrsClassLoader(inputStream);
        }

        val analyzers = new Analyzers();
        analyzers.analyze(classLoader.getClassBytes());

        val transformer = new Transformer();
        val transformedClasses = classLoader.getClassBytes().stream()
                .map(transformer::transform)
                .collect(Collectors.toSet());
        classLoader.setTransformedClasses(transformedClasses);

        val width = parseInt((String) properties.get("applet_minwidth"));
        val height = parseInt((String) properties.get("applet_minheight"));
        val clientClass = classLoader.loadClass(mainClass);
        val applet = (Applet) clientClass.newInstance();
        applet.setStub(new OsrsStub(javConfig, properties));
        applet.setSize(width, height);
        applet.init();

        val window = new JFrame("OldSchool RuneScape");
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.getContentPane().add(applet);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


}
