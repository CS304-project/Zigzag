import Config.GameState;
import Config.GameMode;
import Model.Ball;
import Model.Cube;
import Model.Sound;
import Texture.TextureReader;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

public class ZigzagGLEventListener implements GLEventListener, KeyListener, MouseListener {
    GLCanvas glCanvas;
    ArrayList<Cube> cubes = new ArrayList<>();
    private Ball ball1;
    private Ball ball2;
    private FPSAnimator animator;
    public JLabel counterLabelP1;
    private JLabel counterLabelP2;
    private JPanel scorePanelP1;
    private JPanel scorePanelP2;
    Integer scoreP1 = -1;
    Integer scoreP2 = -1;
    float y = 0;
    boolean isGoingUp = true;
    GameState gameState = GameState.WELCOME;
    private GameMode mode;
    boolean isMuted = false;
    private final String[] textureNames = {
            "Ball//ball.png", "Diamond//WithShadow//Diamond_with_shadow.png", "Home//Info.png", "Pause//Play_button.png",
            "Home//sound_On.png", "Home//TapToPlay.png", "Home//title.png", "EndGame//GameOver.png", "Ball//ball2.png",
            "Pause//Background.png", "Pause//home.png", "GameMode//multiPlayer.png", "GameMode//singlePlayer.png",
            "Home//sound_Off.png"
    };
    private final Sound Tap;
    private final Sound Moving ;
    private final Sound Eating ;
    private final TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    private final int[] textures = new int[textureNames.length];

    public void setGLCanvas(GLCanvas glc) {
        this.glCanvas = glc;
    }

    public void setAnimator(FPSAnimator animator) {
        this.animator = animator;
    }

    public void setScorePanelP1(JPanel scorePanel) {
        this.scorePanelP1 = scorePanel;
    }

    public void setScorePanelP2(JPanel scorePanel) {
        this.scorePanelP2 = scorePanel;
    }

    public ZigzagGLEventListener() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        ball1 = new Ball(
                new Point2D.Float(-0.025f, -0.025f),
                new Point2D.Float(0.025f, -0.025f),
                new Point2D.Float(0.025f, 0.025f),
                new Point2D.Float(-0.025f, 0.025f)
        );
        mode = GameMode.SINGLE_PLAYER;

        initCubes();
       Tap = new Sound("assets/Sounds/start.wav");
       Moving = new Sound("assets/Sounds/moving.wav");
       Eating = new Sound("assets/Sounds/eat_diamond.wav");
    }

    public void setCounterLabelP1(JLabel counterLabel) {
        this.counterLabelP1 = counterLabel;
    }

    public void setCounterLabelP2(JLabel counterLabel) {
        this.counterLabelP2 = counterLabel;
    }

    private void initCubes() {
        int idx = 1;
        Cube firstCube = new Cube(
                new Point2D.Float(0, 0.3f),
                new Point2D.Float(-0.3f, 0),
                new Point2D.Float(0.3f, 0),
                new Point2D.Float(0, -0.3f)
        );

        firstCube.diamond = null;

        firstCube.nextCube = new Cube(
                new Point2D.Float(0.1f, 0.4f),
                new Point2D.Float(0, 0.3f),
                new Point2D.Float(0.2f, 0.3f),
                new Point2D.Float(0.1f, 0.2f),
                Cube.RIGHT
        );

        cubes.add(firstCube);
        cubes.add(firstCube.nextCube);

        for (int i = 0; i < 8; i++) {
            Cube cube = cubes.get(idx);
            idx++;

            cube.generateNewCube();
            cubes.add(cube.nextCube);
        }
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(1, 1, 1, 1);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                final String ASSETS_DIR = "assets//";
                texture[i] = TextureReader.readTexture(ASSETS_DIR + textureNames[i], true);

                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(),
                        texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glLoadIdentity();

        if (gameState == GameState.WELCOME) {
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);

            animateTitle();
            scorePanelP1.setVisible(false);
            scorePanelP2.setVisible(false);

            for (int i = cubes.size() - 1; i >= 0; i--) {
                Cube cube = cubes.get(i);

                cube.drawCube(gl, textures[1]);
            }
            ball1.drawBall(gl, textures[0]);
            if (isMuted){
                drawSoundIcon(gl, textures[13]);
            } else {
                drawSoundIcon(gl, textures[4]);
            }
            drawInfoIcon(gl);
            drawTitle(gl);
            drawClickToPlay(gl);

        } else if (gameState == GameState.PLAYING) {
            Tap.Start();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            scorePanelP1.setVisible(true);
            drawingAnimatingCubes(gl);

            if (mode == GameMode.MULTIPLAYER) {
                scorePanelP2.setVisible(true);
            }
        } else if (gameState == GameState.PAUSED) {
            drawPauseMenu(gl);
        }
    }

    public void drawPauseMenu(GL gl) {
        drawPauseMenuBG(gl);
        if (isMuted){
            PauseMenuSound(gl, textures[13]);
        } else {
            PauseMenuSound(gl, textures[4]);
        }
        PauseMenuHome(gl);
        PauseMenuResume(gl);
    }

    public void drawingAnimatingCubes(GL gl) {
        Cube lastCube = cubes.get(cubes.size() - 1);

        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);

            if (cube.centralMid.y <= -0.6) {
                cube.animateFallingCube(gl, textures[1]);
                if (cube.topMid.y <= -1) {
                    cubes.remove(i);
                }
            } else {
                cube.drawCube(gl, textures[1]);
                cube.animateCube();
                if (cube.diamond != null)
                    cube.diamond.animateDiamond();
            }
        }

        if (mode == GameMode.SINGLE_PLAYER) {
            Cube intersectedCube = null;
            for (Cube cube : cubes) {
                if (cube.isInside(ball1.center)) {
                    intersectedCube = cube;
                    break;
                } else if (cube.nextCube != null && cube.nextCube.isInside(ball1.center)) {
                    intersectedCube = cube.nextCube;
                    break;
                }
            }

            if (intersectedCube == null && animator.isAnimating()) {
                animator.stop();
            } else if (intersectedCube != null && !intersectedCube.hasBeenPassed) {
                scoreP1++;
                counterLabelP1.setText(scoreP1.toString());
                intersectedCube.hasBeenPassed = true;
            }

            if (intersectedCube != null && intersectedCube.diamond != null) {
                float distance = (float) Math.sqrt(Math.pow((ball1.center.x - intersectedCube.diamond.center.x), 2) + Math.pow((ball1.center.y - intersectedCube.diamond.center.y), 2));
                if (distance <= (ball1.radius + intersectedCube.diamond.radius)) {
                    intersectedCube.diamond = null;
                    scoreP1 += 2;
                    Eating.Reset();
                    Eating.Start();
                    counterLabelP1.setText(scoreP1.toString());
                }
            }
        } else {
            Cube intersectedCube1 = null;
            Cube intersectedCube2 = null;

            for (Cube cube : cubes) {
                if (cube.isInside(ball1.center)) {
                    intersectedCube1 = cube;
                    break;
                } else if (cube.nextCube != null && cube.nextCube.isInside(ball1.center)) {
                    intersectedCube1 = cube.nextCube;
                    break;
                }
            }

            for (Cube cube : cubes) {
                if (cube.isInside(ball2.center)) {
                    intersectedCube2 = cube;
                    break;
                } else if (cube.nextCube != null && cube.nextCube.isInside(ball2.center)) {
                    intersectedCube2 = cube.nextCube;
                    break;
                }
            }

            if ((intersectedCube1 == null || intersectedCube2 == null) && animator.isAnimating()) {
                animator.stop();
            } else if (intersectedCube1 != null && !intersectedCube1.hasBeenPassed) {
                scoreP1++;
                scoreP2++;

                intersectedCube1.hasBeenPassed = true;

                counterLabelP1.setText(scoreP1.toString());
                counterLabelP2.setText(scoreP2.toString());
            }

            if (intersectedCube1 != null && intersectedCube1.diamond != null) {
                float distanceP1 = (float) Math.sqrt(
                        Math.pow((ball1.center.x - intersectedCube1.diamond.center.x), 2)
                                + Math.pow((ball1.center.y - intersectedCube1.diamond.center.y), 2)
                );
                float distanceP2 = (float) Math.sqrt(
                        Math.pow((ball2.center.x - intersectedCube1.diamond.center.x), 2)
                                + Math.pow((ball2.center.y - intersectedCube1.diamond.center.y), 2)
                );

                if (distanceP1 <= (ball1.radius + intersectedCube1.diamond.radius)
                        || distanceP2 <= (ball2.radius + intersectedCube1.diamond.radius)) {
                    intersectedCube1.diamond = null;

                    if (distanceP1 < distanceP2) {
                        scoreP1 += 2;
                        Eating.Reset();
                        Eating.Start();
                        counterLabelP1.setText(scoreP1.toString());
                    } else {
                        scoreP2 += 2;
                        Eating.Reset();
                        Eating.Start();
                        counterLabelP2.setText(scoreP2.toString());
                    }
                }
            }
        }

        if (lastCube.centralMid.y - 0.3 <= 1) {
            lastCube.generateNewCube();
            cubes.add(lastCube.nextCube);
        }

        ball1.drawBall(gl, textures[0]);
        ball1.navigateBall();

        if (ball2 != null) {
            ball2.drawBall(gl, textures[8]);
            ball2.navigateBall();
        }
    }

    public void animateTitle() {
        if (isGoingUp) {
            y += 0.0005f;
        } else {
            y -= 0.0005f;
        }
        if (y > 0.05) isGoingUp = false;
        else if (y < -0.05) isGoingUp = true;
    }

    public void drawSoundIcon(GL gl, int texture) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.25, -0.40);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(-0.05, -0.40);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(-0.05, -0.20);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.25, -0.20);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawInfoIcon(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(0.25, -0.40);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.05, -0.40);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.05, -0.20);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(0.25, -0.20);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawTitle(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.25, 0.9);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.25, 0.9);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.25, 0.7);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.25, 0.7);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawClickToPlay(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.15, 0.30 + y);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.15, 0.30 + y);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.15, 0.20 + y);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.15, 0.20 + y);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawPauseMenuBG(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[9]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.5, -0.5);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.5, -0.5);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.5, 0.5);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.5, 0.5);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void PauseMenuSound(GL gl, int texture) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.5, -0.3);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(-0.167, -0.3);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(-0.167, 0.15);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.5, 0.15);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void PauseMenuHome(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[10]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.167, -0.3);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.167, -0.3);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.167, 0.15);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.167, 0.15);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void PauseMenuResume(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(0.167, -0.3);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.5, -0.3);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.5, 0.15);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(0.167, 0.15);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                Moving.Reset();
                Moving.Start();
                ball1.isMovingRight = !ball1.isMovingRight;
            }

            if (e.getKeyCode() == KeyEvent.VK_A) {
                Moving.Reset();
                Moving.Start();
                if (ball2 != null) {
                    ball2.isMovingRight = !ball2.isMovingRight;
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (gameState == GameState.PLAYING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.PLAYING;
            } else if (gameState == GameState.CHOOSE_MODE) {
                gameState = GameState.WELCOME;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        float x = e.getX();
        float y = e.getY();
        Component c = e.getComponent();
        float width = c.getWidth();
        float height = c.getHeight();
        float xPos = (x - width / 2) / width;
        float yPos = (height / 2 - y) / height;
        if (gameState == GameState.WELCOME) {
            if (xPos <= -0.04 && xPos >= -0.108 && yPos <= -0.110 && yPos >= -0.187) {
                isMuted = !isMuted;
            } else {
                gameState = GameState.PLAYING;
            }
        }
        if (gameState == GameState.PAUSED) {
            if (xPos <= 0.22 && xPos >= 0.111 && yPos <= 0.05 && yPos >= -0.122) {
                gameState = GameState.PLAYING;
            } else if (xPos <= 0.05 && xPos >= -0.05 && yPos <= 0.05 && yPos >= -0.122) {
                cubes.clear();
                ball1 = new Ball(
                        new Point2D.Float(-0.025f, -0.025f),
                        new Point2D.Float(0.025f, -0.025f),
                        new Point2D.Float(0.025f, 0.025f),
                        new Point2D.Float(-0.025f, 0.025f)
                );
                initCubes();
                ball2 = null;
                gameState = GameState.WELCOME;
            } else if (xPos <= -0.111 && xPos >= -0.22 && yPos <= 0.05 && yPos >= -0.122) {
                isMuted = !isMuted;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
