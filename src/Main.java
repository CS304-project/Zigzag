import javax.media.opengl.GLCanvas;
import javax.swing.*;

public class Main extends JFrame {
    private final GLCanvas glCanvas;

    public Main() {
        super("Zigzag");

        glCanvas = new GLCanvas();

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