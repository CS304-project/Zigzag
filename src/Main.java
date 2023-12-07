import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;

public class Main extends JFrame  {
    private final GLCanvas glCanvas;
    private final ZigzagGLEventListener listener;

    public Main() {
        super("Zigzag");

        glCanvas = new GLCanvas();
        listener = new ZigzagGLEventListener();
        listener.setGLCanvas(glCanvas);
        glCanvas.addGLEventListener(listener);

        add(glCanvas);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(this);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }


}