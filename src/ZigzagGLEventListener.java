import Config.GameDifficulty;
import Config.GameState;
import Config.GameMode;
import Model.Ball;
import Model.Cube;
import Model.Sound;
import Texture.TextureReader;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
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
import java.io.*;
import java.util.*;

public class ZigzagGLEventListener implements GLEventListener, KeyListener, MouseListener {
    private int countDownTimer = 3, delay = 0, timerCounter = 0;
    private JLabel countDownLabel;
    private final ArrayList<Cube> cubes = new ArrayList<>();
    private Ball blackBall;
    private Ball redBall;
    private FPSAnimator animator;
    public JLabel counterLabelP1;
    private JLabel counterLabelP2;
    private JLabel scoreP1label;
    private JLabel scoreP2label;
    private JLabel highestScoreLabel;
    private JLabel winnerLabel;
    private JPanel scorePanelP1;
    private JPanel scorePanelP2;
    private final Color PINK = new Color(255, 159, 245);
    private final File txtFile;
    private Integer scoreP1 = -1;
    private Integer scoreP2 = -1;
    private String highestScore;
    private float titleYPos = 0;
    boolean isTitleGoingUp = true;
    private GameState gameState = GameState.WELCOME;
    private GameMode mode;
    boolean isMuted = false;
    boolean isInfoMenuOpen = false;
    private GameDifficulty difficulty;
    private final String[] textureNames = {
            "Ball//ball.png", "Diamond//WithShadow//Diamond_with_shadow.png", "Home//Info.png", "Pause//Play_button.png",
            "Home//sound_On.png", "Home//TapToPlay.png", "Home//title.png", "EndGame//GameOver.png", "Ball//ball2.png",
            "Pause//Background.png", "Pause//home.png", "GameMode//multiPlayer.png", "GameMode//singlePlayer.png",
            "Home//sound_Off.png", "EndGame//GameOverM.png", "Home//Info_Menu.png", "Home//levels.png"
    };
    private final Sound clickSound;
    private final Sound movingSound;
    private final Sound collectingDiamondsSound;
    private final Sound fallingSound;
    private final TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    private final int[] textures = new int[textureNames.length];
    private final BitSet keyBits = new BitSet(256);
    private boolean didBlackBallChangeDirection = false;
    private boolean didRedBallChangeDirection = false;

    public ZigzagGLEventListener() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        blackBall = new Ball(
                new Point2D.Float(-0.025f, -0.025f),
                new Point2D.Float(0.025f, -0.025f),
                new Point2D.Float(0.025f, 0.025f),
                new Point2D.Float(-0.025f, 0.025f)
        );
        mode = GameMode.SINGLE_PLAYER;
        clickSound = new Sound("assets/Sounds/start.wav");
        movingSound = new Sound("assets/Sounds/moving.wav");
        collectingDiamondsSound = new Sound("assets/Sounds/eat_diamond.wav");
        fallingSound = new Sound("assets/Sounds/falling.wav");
        highestScore = "0";
        File file = new File("C://3JKM");

        if (file.isDirectory()) {
            txtFile = new File(file.getAbsolutePath() + "//score.txt");
            if (txtFile.isFile()) {
                BufferedReader br = new BufferedReader(new FileReader(txtFile.getAbsoluteFile()));

                if (br.readLine() != null) {
                    Scanner in = new Scanner(txtFile);
                    highestScore = in.nextLine();
                }
            }
        } else {
            boolean isDirCreated = file.mkdir();

            if (isDirCreated) {
                txtFile = new File(file.getAbsolutePath() + "//score.txt");
                boolean isFile = txtFile.createNewFile();
            } else {
                txtFile = null;
            }
        }

        initCubes();
    }

    public void setCountDownLabel(JLabel label) {
        this.countDownLabel = label;
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

    public void setScoreP1label(JLabel scoreP1) {
        this.scoreP1label = scoreP1;
    }

    public void setScoreP2label(JLabel scoreP2) {
        this.scoreP2label = scoreP2;
    }

    public void setWinnerLabel(JLabel winner) {
        this.winnerLabel = winner;
    }

    public void setHighestScoreLabel(JLabel highestScore) {
        this.highestScoreLabel = highestScore;
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

    private void loadAssets(GL gl) {
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

    private void drawWelcomeScreen(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        animateTitle();
        scorePanelP1.setVisible(false);
        scorePanelP2.setVisible(false);
        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);

            cube.draw(gl, textures[1]);
        }
        blackBall.draw(gl, textures[0]);
        if (isMuted) {
            drawSoundIcon(gl, textures[13]);
        } else {
            drawSoundIcon(gl, textures[4]);
        }
        drawInfoIcon(gl);
        drawTitle(gl);
        drawClickToPlay(gl);
        if (isInfoMenuOpen) {
            drawInfoMenu(gl);
        }
    }

    private void drawChooseModeScreen(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);

            cube.draw(gl, textures[1]);
        }
        blackBall.draw(gl, textures[0]);

        drawTitle(gl);
        drawMultiPlayerBTN(gl);
        drawSinglePlayerBTN(gl);
    }

    private void drawChooseDifficultyScreen(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);

            cube.draw(gl, textures[1]);
        }
        blackBall.draw(gl, textures[0]);

        drawTitle(gl);
        drawDifficultyMenu(gl);
    }

    private void drawCountDownScreen(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);
            cube.draw(gl, textures[1]);
        }
        blackBall.draw(gl, textures[0]);
    }


    private void countDown() {
        delay++;
        countDownLabel.setBounds(640, 0, 50, 50);
        countDownLabel.setText("" + countDownTimer);
        countDownLabel.setVisible(true);

        if (delay > 100) {
            delay = 0;
            countDownTimer--;
        }

        if (countDownTimer <= 0) {
            countDownLabel.setVisible(false);
            gameState = GameState.PLAYING;
        }
    }

    private void timer() {
        delay++;
        countDownLabel.setBounds(640, 0, 50, 50);
        countDownLabel.setText("" + timerCounter);
        countDownLabel.setVisible(true);

        if (delay > 100) {
            delay = 0;
            timerCounter++;
        }
    }

    private void drawPlayingScreen(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        scorePanelP1.setVisible(true);

        if (difficulty == GameDifficulty.EASY) {
            handleCubesAndBallsAnimation(gl, 0.002f);
        } else if (difficulty == GameDifficulty.MEDIUM) {
            handleCubesAndBallsAnimation(gl, 0.004f);
        } else if (difficulty == GameDifficulty.HARD) {
            handleCubesAndBallsAnimation(gl, 0.006f);
        }

        if (mode == GameMode.MULTIPLAYER) {
            scorePanelP2.setVisible(true);
        }
    }

    private void handleScoreFileUpdating() {
        if (scoreP1 > Integer.parseInt(highestScore)) {
            highestScore = scoreP1.toString();
            updateScoreFile();
        }
    }

    private void showSinglePlayerSummary() {
        scoreP1label.setOpaque(true);
        scoreP1label.setBackground(PINK);
        highestScoreLabel.setOpaque(true);
        highestScoreLabel.setBackground(PINK);
        scoreP1label.setBounds(700, 306, 50, 50);
        scoreP1label.setText(scoreP1.toString());
        highestScoreLabel.setBounds(750, 360, 50, 50);
        highestScoreLabel.setText(highestScore);
        scoreP1label.setVisible(true);
        highestScoreLabel.setVisible(true);
    }

    private void showMultiPlayerSummary() {
        scoreP1label.setOpaque(true);
        scoreP1label.setBackground(PINK);
        scoreP2label.setOpaque(true);
        scoreP2label.setBackground(PINK);
        winnerLabel.setOpaque(true);
        winnerLabel.setBackground(PINK);
        scoreP1label.setBounds(670, 280, 50, 50);
        scoreP1label.setText(scoreP1.toString());
        scoreP2label.setBounds(670, 335, 50, 50);
        scoreP2label.setText(scoreP2.toString());
        winnerLabel.setBounds(720, 402, 70, 50);
        winnerLabel.setText(redBall.isFalling ? "Black" : blackBall.isFalling ? "Red" : "Draw");
        scoreP1label.setVisible(true);
        scoreP2label.setVisible(true);
        winnerLabel.setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(1, 1, 1, 1);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        loadAssets(gl);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glLoadIdentity();
        handleKeyPressed();

        if (gameState == GameState.WELCOME) {
            drawWelcomeScreen(gl);
        } else if (gameState == GameState.CHOOSE_MODE) {
            drawChooseModeScreen(gl);
        } else if (gameState == GameState.CHOOSE_DIFFICULTY) {
            drawChooseDifficultyScreen(gl);
        } else if (gameState == GameState.COUNTDOWN) {
            drawCountDownScreen(gl);
            countDown();
        } else if (gameState == GameState.PLAYING) {
            timer();
            drawPlayingScreen(gl);
        } else if (gameState == GameState.PAUSED) {
            drawPauseMenu(gl);
        } else if (gameState == GameState.GAME_OVER && animator.isAnimating()) {
            animator.stop();
            if (mode == GameMode.SINGLE_PLAYER) {
                handleScoreFileUpdating();
                drawGameOverMenuS(gl);
                showSinglePlayerSummary();
            } else {
                drawGameOverMenuM(gl);
                showMultiPlayerSummary();
            }
            fallingSound.reset();
        }
    }

    public void drawPauseMenu(GL gl) {
        drawPauseMenuBG(gl);

        if (isMuted) {
            drawPauseMenuSoundBTN(gl, textures[13]);
        } else {
            drawPauseMenuSoundBTN(gl, textures[4]);
        }

        drawPauseMenuHomeBTN(gl);
        drawPauseMenuResumeBTN(gl);
    }

    private void updateScoreFile() {
        try {
            if (txtFile != null) {
                PrintWriter out = new PrintWriter(txtFile.getAbsoluteFile());

                out.println(scoreP1);
                out.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private void handleBlackBallFallingAnimation(GL gl) {
        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);
            cube.draw(gl, textures[1]);
        }
        if (!isMuted) {
            fallingSound.start();
        }

        blackBall.animateFalling();

        if (blackBall.topLeft.y < -1) {
            gameState = GameState.GAME_OVER;
        }
    }

    private void handleRedBallFallingAnimation(GL gl) {
        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);
            cube.draw(gl, textures[1]);
        }
        if (!isMuted) {
            fallingSound.start();
        }

        redBall.animateFalling();

        if (redBall.topLeft.y < -1) {
            gameState = GameState.GAME_OVER;
        }
    }

    private void handleGamePlayAnimation(GL gl, float speed) {
        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);

            if (cube.centralMid.y <= -0.6) {
                cube.animateExiting(gl, textures[1], speed);
                if (cube.diamond != null) {
                    cube.diamond.animateExiting(speed);
                }
                if (cube.topMid.y <= -1) {
                    cubes.remove(i);
                }
            } else {
                cube.draw(gl, textures[1]);
                cube.animate(speed);
                if (cube.diamond != null)
                    cube.diamond.animate(speed);
            }
        }

        blackBall.navigate(speed);

        if (redBall != null) redBall.navigate(speed);
    }

    private Cube getIntersectedCube(Ball ball) {
        Cube intersectedCube = null;

        for (Cube cube : cubes) {
            if (cube.isInside(ball.center)) {
                intersectedCube = cube;
                break;
            } else if (cube.nextCube != null && cube.nextCube.isInside(ball.center)) {
                intersectedCube = cube.nextCube;
                break;
            }
        }

        return intersectedCube;
    }

    private void handleBlackBallBoundariesInSinglePlayerMode() {
        Cube intersectedCube = getIntersectedCube(blackBall);

        if (intersectedCube == null) {
            blackBall.isFalling = true;
        } else if (!intersectedCube.hasBeenPassed) {
            scoreP1++;
            counterLabelP1.setText(scoreP1.toString());
            intersectedCube.hasBeenPassed = true;
        }

        if (intersectedCube != null && intersectedCube.diamond != null) {
            float distance = (float) Math.sqrt(
                    Math.pow((blackBall.center.x - intersectedCube.diamond.center.x), 2)
                            + Math.pow((blackBall.center.y - intersectedCube.diamond.center.y), 2));
            if (distance <= (blackBall.radius + intersectedCube.diamond.radius)) {
                intersectedCube.diamond = null;
                scoreP1 += 2;

                if (!isMuted) {
                    collectingDiamondsSound.reset();
                    collectingDiamondsSound.start();
                }

                counterLabelP1.setText(scoreP1.toString());
            }
        }
    }

    private void handleBallsBoundariesInMultiPlayerMode() {
        Cube intersectedCubeWithBlack = getIntersectedCube(blackBall);
        Cube intersectedCubeWithRed = getIntersectedCube(redBall);

        if ((intersectedCubeWithBlack == null || intersectedCubeWithRed == null)) {
            if (intersectedCubeWithBlack == null) {
                blackBall.isFalling = true;
            } else {
                redBall.isFalling = true;
            }
        } else if (!intersectedCubeWithBlack.hasBeenPassed) {
            scoreP1++;
            scoreP2++;
            intersectedCubeWithBlack.hasBeenPassed = true;

            counterLabelP1.setText(scoreP1.toString());
            counterLabelP2.setText(scoreP2.toString());
        }

        if (intersectedCubeWithBlack != null && intersectedCubeWithBlack.diamond != null) {
            float distanceP1 = (float) Math.sqrt(
                    Math.pow((blackBall.center.x - intersectedCubeWithBlack.diamond.center.x), 2)
                            + Math.pow((blackBall.center.y - intersectedCubeWithBlack.diamond.center.y), 2)
            );
            float distanceP2 = (float) Math.sqrt(
                    Math.pow((redBall.center.x - intersectedCubeWithBlack.diamond.center.x), 2)
                            + Math.pow((redBall.center.y - intersectedCubeWithBlack.diamond.center.y), 2)
            );

            if (distanceP1 <= (blackBall.radius + intersectedCubeWithBlack.diamond.radius)
                    || distanceP2 <= (redBall.radius + intersectedCubeWithBlack.diamond.radius)) {
                intersectedCubeWithBlack.diamond = null;

                if (distanceP1 < distanceP2) {
                    scoreP1 += 2;

                    if (!isMuted) {
                        collectingDiamondsSound.reset();
                        collectingDiamondsSound.start();
                    }

                    counterLabelP1.setText(scoreP1.toString());
                } else {
                    scoreP2 += 2;

                    if (!isMuted) {
                        collectingDiamondsSound.reset();
                        collectingDiamondsSound.start();
                    }

                    counterLabelP2.setText(scoreP2.toString());
                }
            }
        }
    }

    public void handleCubesAndBallsAnimation(GL gl, float speed) {
        Cube lastCube = cubes.get(cubes.size() - 1);

        if (blackBall.isFalling) {
            handleBlackBallFallingAnimation(gl);
        } else if (redBall != null && redBall.isFalling) {
            handleRedBallFallingAnimation(gl);
        } else {
            handleGamePlayAnimation(gl, speed);
        }

        if (mode == GameMode.SINGLE_PLAYER) {
            handleBlackBallBoundariesInSinglePlayerMode();
        } else {
            handleBallsBoundariesInMultiPlayerMode();
        }

        if (lastCube.centralMid.y - 0.3 <= 1) {
            lastCube.generateNewCube();
            cubes.add(lastCube.nextCube);
        }

        drawBalls(gl);
    }

    private void drawBalls(GL gl) {
        blackBall.draw(gl, textures[0]);

        if (redBall != null) {
            redBall.draw(gl, textures[8]);
        }
    }

    public void animateTitle() {
        if (isTitleGoingUp) {
            titleYPos += 0.0005f;
        } else {
            titleYPos -= 0.0005f;
        }

        if (titleYPos > 0.05) isTitleGoingUp = false;
        else if (titleYPos < -0.05) isTitleGoingUp = true;
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
        gl.glVertex2d(-0.15, 0.30 + titleYPos);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.15, 0.30 + titleYPos);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.15, 0.20 + titleYPos);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.15, 0.20 + titleYPos);
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

    public void drawPauseMenuSoundBTN(GL gl, int texture) {
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

    public void drawPauseMenuHomeBTN(GL gl) {
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

    public void drawPauseMenuResumeBTN(GL gl) {
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

    public void drawGameOverMenuS(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[7]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.4, -0.7);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.4, -0.7);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.4, 0.7);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.4, 0.7);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawGameOverMenuM(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[14]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.4, -0.7);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.4, -0.7);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.4, 0.7);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.4, 0.7);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawMultiPlayerBTN(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[11]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.8, -0.3);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(-0.167, -0.3);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(-0.167, 0.15);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.8, 0.15);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawSinglePlayerBTN(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[12]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(0.167, -0.3);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.8, -0.3);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.8, 0.15);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(0.167, 0.15);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawInfoMenu(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 2]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.5, -0.9);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.5, -0.9);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.5, 0.9);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.5, 0.9);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawDifficultyMenu(GL gl) {
        gl.glEnable(gl.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(-0.5, -0.75);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(0.5, -0.75);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(0.5, 0.75);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(-0.5, 0.75);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void reset() {
        cubes.clear();

        blackBall = new Ball(
                new Point2D.Float(-0.025f, -0.025f),
                new Point2D.Float(0.025f, -0.025f),
                new Point2D.Float(0.025f, 0.025f),
                new Point2D.Float(-0.025f, 0.025f)
        );
        redBall = null;
        scoreP1 = -1;
        scoreP2 = -1;
        delay = 0;
        timerCounter = 0;
        countDownTimer = 3;

        scoreP2label.setVisible(false);
        scoreP1label.setVisible(false);
        winnerLabel.setVisible(false);
        highestScoreLabel.setVisible(false);
        countDownLabel.setVisible(false);

        initCubes();
        gameState = GameState.WELCOME;
    }

    private void handleMouseClicksInWelcomeScreen(float xPos, float yPos) {
        if (xPos <= -0.04 && xPos >= -0.108 && yPos <= -0.110 && yPos >= -0.187 && !isInfoMenuOpen) {
            if (isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            isMuted = !isMuted;
        } else if (xPos <= 0.108 && xPos >= 0.041 && yPos <= -0.110 && yPos >= -0.187 && !isInfoMenuOpen) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            isInfoMenuOpen = true;
        } else if (isInfoMenuOpen && xPos <= 0.08 && xPos >= -0.08 && yPos <= -0.232 && yPos >= -0.354) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            isInfoMenuOpen = false;
        } else if (!isInfoMenuOpen) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            gameState = GameState.CHOOSE_MODE;
        }
    }

    private void handleMouseClicksInChooseModeScreen(float xPos, float yPos) {
        if (xPos <= 0.391 && xPos >= 0.091 && yPos <= 0.06 && yPos >= -0.132) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            mode = GameMode.SINGLE_PLAYER;
            gameState = GameState.CHOOSE_DIFFICULTY;
        } else if (xPos <= -0.091 && xPos >= -0.391 && yPos <= 0.06 && yPos >= -0.132) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            redBall = new Ball(
                    new Point2D.Float(-0.05f, -0.025f),
                    new Point2D.Float(0, -0.025f),
                    new Point2D.Float(0, 0.025f),
                    new Point2D.Float(-0.05f, 0.025f)
            );
            mode = GameMode.MULTIPLAYER;
            gameState = GameState.CHOOSE_DIFFICULTY;
        }
    }

    private void handleMouseClicksInChooseDifficultyScreen(float xPos, float yPos) {
        if (xPos <= 0.102 && xPos >= -0.097 && yPos <= 0.243 && yPos >= 0.130) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            difficulty = GameDifficulty.EASY;
            gameState = GameState.COUNTDOWN;
        } else if (xPos <= 0.102 && xPos >= -0.097 && yPos <= 0.057 && yPos >= -0.057) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            difficulty = GameDifficulty.MEDIUM;
            gameState = GameState.COUNTDOWN;
        } else if (xPos <= 0.102 && xPos >= -0.097 && yPos <= -0.131 && yPos >= -0.241) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            difficulty = GameDifficulty.HARD;
            gameState = GameState.COUNTDOWN;
        }
    }

    private void handleMouseClicksInPauseModal(float xPos, float yPos) {
        if (xPos <= 0.22 && xPos >= 0.111 && yPos <= 0.05 && yPos >= -0.122) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            gameState = GameState.PLAYING;
        } else if (xPos <= 0.05 && xPos >= -0.05 && yPos <= 0.05 && yPos >= -0.122) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            reset();
        } else if (xPos <= -0.111 && xPos >= -0.22 && yPos <= 0.05 && yPos >= -0.122) {
            if (isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            isMuted = !isMuted;
        }
    }

    private void handleMouseClicksInLoseModal(float xPos, float yPos) {
        if (xPos <= 0.079 && xPos >= -0.076 && yPos <= -0.094 && yPos >= -0.194) {
            if (!isMuted) {
                clickSound.reset();
                clickSound.start();
            }

            reset();
            animator.start();
        }
    }

    private void handleKeyPressed() {
        if (gameState == GameState.PLAYING) {
            if (keyBits.get(KeyEvent.VK_SPACE) && !didBlackBallChangeDirection) {
                didBlackBallChangeDirection = true;

                if (!isMuted) {
                    movingSound.reset();
                    movingSound.start();
                }

                blackBall.isMovingRight = !blackBall.isMovingRight;
            }

            if (keyBits.get(KeyEvent.VK_A) && !didRedBallChangeDirection) {
                didRedBallChangeDirection = true;

                if (!isMuted) {
                    movingSound.reset();
                    movingSound.start();
                }

                if (redBall != null) {
                    redBall.isMovingRight = !redBall.isMovingRight;
                }
            }
        }

        if (keyBits.get(KeyEvent.VK_ESCAPE)) {
            if (gameState == GameState.PLAYING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.PLAYING;
            } else if (gameState == GameState.CHOOSE_MODE) {
                gameState = GameState.WELCOME;
            } else if (gameState == GameState.CHOOSE_DIFFICULTY) {
                gameState = GameState.CHOOSE_MODE;
            }
        }
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
        if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
            keyBits.set(e.getKeyCode());
        } else if (gameState == GameState.PLAYING){
            gameState = GameState.PAUSED;
        } else if (gameState == GameState.PAUSED){
            gameState = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyBits.clear(e.getKeyCode());

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            didBlackBallChangeDirection = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            didRedBallChangeDirection = false;
        }
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
            handleMouseClicksInWelcomeScreen(xPos, yPos);
        } else if (gameState == GameState.CHOOSE_MODE) {
            handleMouseClicksInChooseModeScreen(xPos, yPos);
        } else if (gameState == GameState.CHOOSE_DIFFICULTY) {
            handleMouseClicksInChooseDifficultyScreen(xPos, yPos);
        } else if (gameState == GameState.PAUSED) {
            handleMouseClicksInPauseModal(xPos, yPos);
        } else if (gameState == GameState.GAME_OVER) {
            handleMouseClicksInLoseModal(xPos, yPos);
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