import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame  {
    public Main() {
        super("Zigzag");

        try {
            GLCanvas glCanvas = new GLCanvas();
            ZigzagGLEventListener listener = new ZigzagGLEventListener();
            FPSAnimator animator = new FPSAnimator(glCanvas, 265);
            JLabel counterLabelP1 = new JLabel();
            JLabel counterLabelP2 = new JLabel();

            counterLabelP1.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));
            counterLabelP2.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));

            JLabel scoreLabelP1 = new JLabel("score: ");
            JLabel scoreLabelP2 = new JLabel("score: ");

            scoreLabelP1.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));
            scoreLabelP2.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));

            JPanel jPanelP1 = new JPanel();
            JPanel jPanelP2 = new JPanel();

            jPanelP1.add(scoreLabelP1);
            jPanelP1.add(counterLabelP1);
            jPanelP1.setBackground(Color.white);
            jPanelP1.setVisible(false);

            jPanelP2.add(scoreLabelP2);
            jPanelP2.add(counterLabelP2);
            jPanelP2.setBackground(Color.white);
            jPanelP2.setVisible(false);

            listener.setScorePanelP1(jPanelP1);
            listener.setScorePanelP2(jPanelP2);
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
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException : " + e.getMessage());
        }catch (UnsupportedAudioFileException e) {
            System.out.println("UnsupportedAudioFileException : " + e.getMessage());
        }catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}