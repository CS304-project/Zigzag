import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {
    public Main() {
        super("Zigzag");

        try {
            GLCanvas glCanvas = new GLCanvas();
            ZigzagGLEventListener listener = new ZigzagGLEventListener();
            FPSAnimator animator = new FPSAnimator(glCanvas, 100);
            JLabel counterLabelP1 = new JLabel();
            JLabel counterLabelP2 = new JLabel();
            JLayeredPane lp = new JLayeredPane();
            JPanel jPanelP1 = new JPanel();
            JPanel jPanelP2 = new JPanel();
            JLabel scoreLabelP1 = new JLabel("score: ");
            JLabel scoreLabelP2 = new JLabel("score: ");
            JLabel gameOverScoreP1Label = new JLabel("");
            JLabel gameOverScoreP2Label = new JLabel("");
            JLabel highestScoreLabel = new JLabel("");
            JLabel winnerLabel = new JLabel("");
            JLabel countDownLabel = new JLabel();
            Font font = new Font("Myriad Arabic", Font.PLAIN, 24);

            countDownLabel.setFont(font);
            countDownLabel.setBackground(Color.WHITE);

            counterLabelP1.setFont(font);
            counterLabelP1.setOpaque(true);
            counterLabelP1.setBackground(Color.white);

            counterLabelP2.setFont(font);
            counterLabelP2.setForeground(Color.RED);

            scoreLabelP1.setFont(font);
            scoreLabelP1.setBackground(Color.WHITE);

            scoreLabelP2.setFont(font);
            scoreLabelP2.setForeground(Color.RED);

            gameOverScoreP1Label.setFont(font);

            gameOverScoreP2Label.setFont(font);

            highestScoreLabel.setFont(font);

            winnerLabel.setFont(font);

            jPanelP1.add(scoreLabelP1);
            jPanelP1.add(counterLabelP1);
            jPanelP1.setBounds(1080, 0, 150, 40);
            jPanelP1.setOpaque(true);
            jPanelP1.setBackground(new Color(255, 255, 255));
            jPanelP1.setVisible(false);

            jPanelP2.add(scoreLabelP2);
            jPanelP2.add(counterLabelP2);
            jPanelP2.setBounds(0, 0, 150, 40);
            jPanelP2.setOpaque(true);
            jPanelP2.setBackground(new Color(255, 255, 255));
            jPanelP2.setVisible(false);

            listener.setScorePanelP1(jPanelP1);
            listener.setScorePanelP2(jPanelP2);
            listener.setCounterLabelP1(counterLabelP1);
            listener.setCounterLabelP2(counterLabelP2);
            listener.setAnimator(animator);
            listener.setScoreP1label(gameOverScoreP1Label);
            listener.setScoreP2label(gameOverScoreP2Label);
            listener.setHighestScoreLabel(highestScoreLabel);
            listener.setWinnerLabel(winnerLabel);
            listener.setCountDownLabel(countDownLabel);

            glCanvas.addGLEventListener(listener);
            glCanvas.addMouseListener(listener);
            glCanvas.addKeyListener(listener);
            glCanvas.setBounds(0, 0, 1280, 800);
            glCanvas.setFocusable(true);

            lp.setPreferredSize(new Dimension(1280, 800));
            lp.add(jPanelP1, 0);
            lp.add(jPanelP2, 1);
            lp.add(gameOverScoreP1Label, 2);
            lp.add(gameOverScoreP2Label, 3);
            lp.add(winnerLabel, 4);
            lp.add(highestScoreLabel, 5);
            lp.add(countDownLabel, 6);
            lp.add(glCanvas, 7);

            add(lp, BorderLayout.CENTER);
            setLocationRelativeTo(this);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1280, 800);
            setLocationRelativeTo(this);
            setVisible(true);
            animator.start();
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException: " + e.getMessage());
        } catch (UnsupportedAudioFileException e) {
            System.out.println("UnsupportedAudioFileException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}