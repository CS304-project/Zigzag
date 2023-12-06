import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame  {
    private final GLCanvas glCanvas;
    private final ZigzagGLEventListener listener;

    public Main() {
        super("Zigzag");

        glCanvas = new GLCanvas();
        listener = new ZigzagGLEventListener();
        listener.setGLCanvas(glCanvas);

        add(glCanvas);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 889);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }


}