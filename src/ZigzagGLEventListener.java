import Model.Ball;
import Model.Cube;
import Texture.TextureReader;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
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
    private final Ball ball;
    private  FPSAnimator animator;
    private final String[] textureNames = {
            "Ball//ball.png", "Diamond//WithShadow//Diamond.png", "HowToPlay//Info.png", "Play//Play_button.png",
            "Sound//sound_On.png"
    };
    private final TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    private final int[] textures = new int[textureNames.length];

    public void setGLCanvas(GLCanvas glc) {
        this.glCanvas = glc;
    }

    public ZigzagGLEventListener() {
        ball = new Ball(
                new Point2D.Double(-0.1, -0.1),
                new Point2D.Double(0.1, -0.1),
                new Point2D.Double(0.1, 0.1),
                new Point2D.Double(-0.1, 0.1)
        );
        initCubes();

    }

    private void initCubes() {
        int idx = 1;

        cubes.add(new Cube(
                new Point2D.Double(0, 0.3),
                new Point2D.Double(-0.3, 0),
                new Point2D.Double(0.3, 0),
                new Point2D.Double(0, -0.3)

        ));

        cubes.add(new Cube(
                new Point2D.Double(0.1, 0.4),
                new Point2D.Double(0, 0.3),
                new Point2D.Double(0.2, 0.3),
                new Point2D.Double(0.1, 0.2)
        ));

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
        animator = new FPSAnimator(glCanvas, 256);
        animator.start();
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

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        //TODO: Start rendering
        for (int i = cubes.size() - 1; i >= 0; i--) {
            Cube cube = cubes.get(i);

            cube.drawCube(gl);
            cube.animateCube();
        }

        ball.drawBall(gl, textures[0]);
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

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
