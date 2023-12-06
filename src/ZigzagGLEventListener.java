import Texture.TextureReader;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;

public class ZigzagGLEventListener implements GLEventListener {
    GLCanvas glCanvas;
    private final String[] textureNames = {
            "Ball//ball.png", "Diamond//WithShadow//Diamond.png", "HowToPlay//Info.png", "Play//Play_button.png",
            "Sound//sound_On.png"
    };
    private final TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    private final int[] textures = new int[textureNames.length];

    public void setGLCanvas(GLCanvas glc) {

        this.glCanvas = glc;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }
}
