package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Cube {
    public Point2D.Double topMid;
    public Point2D.Double topLeft;
    public Point2D.Double topRight;
    public Point2D.Double centralMid;
    public Point2D.Double centerTileP;
    boolean hasADiamond;
    public Cube nextCube;

    public Cube(Point2D.Double topMid,
                Point2D.Double topLeft,
                Point2D.Double topRight,
                Point2D.Double centralMid) {

        this.topMid = topMid;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.centralMid = centralMid;

    }

    public void drawCube(GL gl) {
        drawTile(gl);
        drawRProjection(gl);
        drawLProjection(gl);
    }

    public void drawTile(GL gl){
        gl.glColor3f(0.572f, 0.929f, 1);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(topMid.x, topMid.y);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glEnd();
    }

    public void drawRProjection(GL gl){
        gl.glColor3f(0.298f, 0.522f, 0.784f);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glVertex2d(topRight.x, topRight.y - 0.4);
        gl.glVertex2d(centralMid.x, centralMid.y - 0.4);
        gl.glEnd();
    }

    public void drawLProjection(GL gl){
        gl.glColor3f(0.250f, 0.431f, 0.635f);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glVertex2d(topLeft.x, topLeft.y - 0.4);
        gl.glVertex2d(centralMid.x, centralMid.y - 0.4);
        gl.glEnd();
    }

    public void generateDiamond() {

        // TODO
    }

    public void generateNewCube() {
        int randomNumber = (int) (Math.random() * 20);
        double horizontalD = Math.sqrt(Math.pow(topRight.x - topLeft.x, 2) + Math.pow(topRight.y - topLeft.y, 2));
        double verticalD = Math.sqrt(Math.pow(topMid.x - centralMid.x, 2) + Math.pow(topMid.y - centralMid.y, 2));
        if (randomNumber < 10) {
            nextCube = new Cube(
                    new Point2D.Double(topRight.x, topRight.y + verticalD),
                    topMid,
                    new Point2D.Double(topMid.x + horizontalD, topMid.y),
                    topRight
            );
        } else {
            nextCube = new Cube(
                    new Point2D.Double(topLeft.x, topLeft.y + verticalD),
                    new Point2D.Double(topMid.x - horizontalD, topMid.y),
                    topMid,
                    topLeft
            );
        }
    }
}
