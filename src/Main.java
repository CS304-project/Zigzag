import Model.Cube;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import java.awt.geom.Point2D;

public class Main extends JFrame  {
    private final GLCanvas glCanvas;
    private final ZigzagGLEventListener listener;
    private FPSAnimator animator;


    public Main() {
        super("Zigzag");

        glCanvas = new GLCanvas();
        listener = new ZigzagGLEventListener();
        animator = new FPSAnimator(glCanvas, 60);

        listener.setGLCanvas(glCanvas);
        listener.setAnimator(animator);
        glCanvas.addGLEventListener(listener);
        glCanvas.addMouseListener(listener);
        add(glCanvas);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(this);
        setVisible(true);
        animator.start();
    }

    public static void main(String[] args) {
        new Main();
//        Cube firstCube = new Cube(
//                new Point2D.Double(0, 0.3),
//                new Point2D.Double(-0.3, 0),
//                new Point2D.Double(0.3, 0),
//                new Point2D.Double(0, -0.3)
//        );
//
//        System.out.println(firstCube.isInside(new Point2D.Double(0.4, 0.0)));
    }
}