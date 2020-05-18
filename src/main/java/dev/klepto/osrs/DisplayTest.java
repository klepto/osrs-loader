package dev.klepto.osrs;

import dev.klepto.osrs.internal.RSCanvasBufferProvider;
import dev.klepto.osrs.internal.RSClient;
import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class DisplayTest extends JComponent {

    private final RSClient client;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public DisplayTest(RSClient client) {
        this.client = client;
        setPreferredSize(new Dimension(765, 503));
        service.scheduleAtFixedRate(this::repaint, 0, 15, TimeUnit.MILLISECONDS);
    }

    public void paint(Graphics g) {
        val bufferProvider = (RSCanvasBufferProvider) client.getBufferProvider();
        g.drawImage(bufferProvider.getImage(), 0, 0, null);
    }

}