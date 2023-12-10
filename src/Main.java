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
            JLayeredPane lp = new JLayeredPane();
            JPanel jPanelP1 = new JPanel();
            JPanel jPanelP2 = new JPanel();
            JLabel scoreLabelP1 = new JLabel("score: ");
            JLabel scoreLabelP2 = new JLabel("score: ");
            JLabel scoreP1 = new JLabel("");
            JLabel scoreP2 = new JLabel("");
            JLabel highestScore = new JLabel("");
            JLabel winner = new JLabel("");

            counterLabelP1.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));

            counterLabelP2.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));
            counterLabelP2.setForeground(Color.RED);

            scoreLabelP1.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));


            scoreLabelP2.setFont(new Font("Myriad Arabic", Font.PLAIN, 24));
            scoreLabelP2.setForeground(Color.RED);


            jPanelP1.add(scoreLabelP1);
            jPanelP1.add(counterLabelP1);
            jPanelP1.setBounds(1080, 0, 150, 40);
            jPanelP1.setOpaque(false);
            jPanelP1.setVisible(false);

            jPanelP2.add(scoreLabelP2);
            jPanelP2.add(counterLabelP2);
            jPanelP2.setBounds(0, 0, 150, 40);
            jPanelP2.setOpaque(false);

            jPanelP2.setVisible(false);

            listener.setScorePanelP1(jPanelP1);
            listener.setScorePanelP2(jPanelP2);
            listener.setCounterLabelP1(counterLabelP1);
            listener.setCounterLabelP2(counterLabelP2);
            listener.setGLCanvas(glCanvas);
            listener.setAnimator(animator);
            listener.setScoreP1label(scoreP1);
            listener.setScoreP2label(scoreP2);
            listener.setHighestScorelabel(highestScore);
            listener.setWinnerlabel(winner);

            glCanvas.addGLEventListener(listener);
            glCanvas.addMouseListener(listener);
            glCanvas.addKeyListener(listener);
            glCanvas.setBounds(0, 0, 1280, 800);
            glCanvas.setFocusable(true);

//            scoreP1.setBounds(700,306,50,50); in single mode
//            highestScore.setBounds(750,360,50,50);
//            scoreP1.setBounds(670,280,50,50); in  multi
//            scoreP2.setBounds(670,335,50,50);
//            winner.setBounds(720,402,50,50);

            lp.setPreferredSize(new Dimension(1280, 800));
            lp.add(jPanelP1, 0);
            lp.add(jPanelP2, 1);
            lp.add(scoreP1,2);
            lp.add(scoreP2,3);
            lp.add(winner,4);
            lp.add(highestScore,5);
            lp.add(glCanvas, 6);
            add(lp, BorderLayout.CENTER);
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