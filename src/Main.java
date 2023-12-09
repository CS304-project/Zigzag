import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame  {
    private final GLCanvas glCanvas;
    private final ZigzagGLEventListener listener;
    private FPSAnimator animator;
    public final JLabel counterLabel;
    public final JLabel scoreLabel;
    private final JPanel jPanel;
    public Main() {
        super("Zigzag");

        glCanvas = new GLCanvas();
        listener = new ZigzagGLEventListener();
        animator = new FPSAnimator(glCanvas, 265);
        counterLabel = new JLabel("0");
        scoreLabel = new JLabel("score: ");
        jPanel = new JPanel();
        jPanel.add(scoreLabel);
        jPanel.add(counterLabel);
        jPanel.setOpaque(true);
        jPanel.setBackground(Color.white);
        jPanel.setVisible(false);
        listener.setCounterLabel(counterLabel);
        listener.setScorePanel(jPanel);
        listener.setGLCanvas(glCanvas);
        listener.setAnimator(animator);
        glCanvas.addGLEventListener(listener);
        glCanvas.addMouseListener(listener);
        add(glCanvas);
        add(jPanel, BorderLayout.EAST);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(this);
        setVisible(true);
        animator.start();
    }

    public static void main(String[] args) {
        new Main();
    }
}