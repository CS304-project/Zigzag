package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Cube {
    public Point2D.Double topMid;
    public Point2D.Double topLeft;
    public Point2D.Double topRight;
    public Point2D.Double centralMid;
    public Point2D.Double bottomMid;
    public Point2D.Double bottomLeft;
    public Point2D.Double bottomRight;
    public Point2D.Double centerTileP;
    boolean hasADiamond;
    Cube nextCube;

    public Cube(Point2D.Double topMid,
                Point2D.Double topLeft,
                Point2D.Double topRight,
                Point2D.Double centralMid,
                Point2D.Double bottomMid,
                Point2D.Double bottomLeft,
                Point2D.Double bottomRight) {

        this.topMid = topMid;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.centralMid = centralMid;
        this.bottomMid = bottomMid;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public void drawCube(GL gl) {

        gl.glColor3f(0.572f, 0.929f, 1);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(topMid.x, topMid.y);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glEnd();

        gl.glColor3f(0.298f, 0.522f, 0.784f);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glVertex2d(bottomRight.x, bottomRight.y);
        gl.glVertex2d(bottomMid.x, bottomMid.y);
        gl.glEnd();

        gl.glColor3f(0.250f, 0.431f, 0.635f);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glVertex2d(bottomLeft.x, bottomLeft.y);
        gl.glVertex2d(bottomMid.x, bottomMid.y);
        gl.glEnd();
    }

    public void generateDiamond() {

        // TODO
    }

    public void generateNewCube() {
        int randomNumber = (int) (Math.random() * 20);
        int horizontalD = (int) Math.sqrt(Math.pow(topRight.x - topLeft.x, 2) + Math.pow(topRight.y - topLeft.y, 2));
        int verticalD = (int) Math.sqrt(Math.pow(topMid.x - centralMid.x, 2) + Math.pow(topMid.y - centralMid.y, 2));
        if (randomNumber < 10) {
            nextCube = new Cube(
                    new Point2D.Double(topRight.x, topRight.y + verticalD),
                    topMid,
                    new Point2D.Double(topMid.x + horizontalD, topMid.y),
                    topRight,
                    new Point2D.Double(topRight.x, topRight.y - 4),
                    new Point2D.Double(topLeft.x, topLeft.y - 4),
                    new Point2D.Double(topRight.x + horizontalD, topRight.y - 4)
            );
        } else {
            nextCube = new Cube(
                    new Point2D.Double(topLeft.x, topLeft.y + verticalD),
                    new Point2D.Double(topMid.x - horizontalD, topMid.y),
                    topMid,
                    topLeft,
                    new Point2D.Double(topLeft.x, topLeft.y - 4),
                    new Point2D.Double(topMid.x - horizontalD, topMid.y - 4),
                    new Point2D.Double(topMid.x, topMid.y - 4)
            );
        }
    }
}
