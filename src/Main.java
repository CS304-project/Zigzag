import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame  {
    private final GLCanvas glCanvas;
    private final ZigzagGLEventListener listener;
    private FPSAnimator animator;
    public final JLabel counterLabelP1;
    private final JLabel counterLabelP2;
    private final JLabel scoreLabelP1;
    private final JLabel scoreLabelP2;
    private final JPanel jPanelP1;
    private final JPanel jPanelP2;
    public Main() {
        super("Zigzag");

        glCanvas = new GLCanvas();
        listener = new ZigzagGLEventListener();
        animator = new FPSAnimator(glCanvas, 265);

        counterLabelP1 = new JLabel();
        counterLabelP2 = new JLabel();
        scoreLabelP1 = new JLabel("score: ");
        scoreLabelP2 = new JLabel("score: ");
        jPanelP1 = new JPanel();
        jPanelP2 = new JPanel();

        jPanelP1.add(scoreLabelP1);
        jPanelP1.add(counterLabelP1);
        jPanelP1.setBackground(Color.white);

        jPanelP2.add(scoreLabelP2);
        jPanelP2.add(counterLabelP2);
        jPanelP2.setBackground(Color.white);

        listener.setCounterLabelP1(counterLabelP1);
        listener.setCounterLabelP2(counterLabelP2);
        listener.setGLCanvas(glCanvas);
        listener.setAnimator(animator);

        glCanvas.addGLEventListener(listener);
        glCanvas.addMouseListener(listener);
        glCanvas.addKeyListener(listener);
        glCanvas.setFocusable(true);

        add(glCanvas);
        add(jPanelP1, BorderLayout.EAST);
        add(jPanelP2, BorderLayout.WEST);
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