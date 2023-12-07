package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Cube {
    public Point2D.Double topMid;
    public Point2D.Double topLeft;
    public Point2D.Double topRight;
    public Point2D.Double centralMid;
    public Point2D.Double botMid;
    public Point2D.Double botLeft;
    public Point2D.Double botRight;
    public Point2D.Double centerTileP;
    public Cube nextCube;
    public Diamond diamond;

    public Cube(Point2D.Double topMid,
                Point2D.Double topLeft,
                Point2D.Double topRight,
                Point2D.Double centralMid) {

        this.topMid = topMid;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.centralMid = centralMid;
        this.botMid = new Point2D.Double(centralMid.x, centralMid.y - 0.3);
        this.botLeft = new Point2D.Double(topLeft.x, topLeft.y - 0.3);
        this.botRight = new Point2D.Double(topRight.x, topRight.y - 0.3);
        generateDiamond();
    }
    public void drawCube(GL gl, int texture) {
        drawTile(gl);
        drawRProjection(gl);
        drawLProjection(gl);
        if (diamond != null) {
            diamond.drawDiamond(gl, texture);
            diamond.animateDiamond();
        }

    }

    public void drawTile(GL gl) {
        gl.glColor3f(0.572f, 0.929f, 1);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(topMid.x, topMid.y);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glEnd();
    }

    public void drawRProjection(GL gl) {
        gl.glColor3f(0.298f, 0.522f, 0.784f);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glVertex2d(botRight.x, botRight.y);
        gl.glVertex2d(botMid.x, botMid.y);
        gl.glEnd();
    }

    public void drawLProjection(GL gl) {
        gl.glColor3f(0.250f, 0.431f, 0.635f);
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex2d(centralMid.x, centralMid.y);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glVertex2d(botLeft.x, botLeft.y);
        gl.glVertex2d(botMid.x, botMid.y);
        gl.glEnd();
    }

    public void generateDiamond() {
        int randomNumber = (int) (Math.random() * 10);
        if (randomNumber > 6) {
            diamond = new Diamond(
                    new Point2D.Double((topLeft.x + centralMid.x) / 2, (topLeft.y + centralMid.y) / 2),
                    new Point2D.Double((centralMid.x + topRight.x) / 2, (centralMid.y + topRight.y) / 2),
                    new Point2D.Double((topRight.x + topMid.x) / 2, (topRight.y + topMid.y) / 2),
                    new Point2D.Double((topMid.x + topLeft.x) / 2, (topMid.y + topLeft.y) / 2)
            );
        }

    }

    public void generateNewCube() {
        int randomNumber = (int) (Math.random() * 20);
        double horizontalD = Math.sqrt(Math.pow(topRight.x - topLeft.x, 2) + Math.pow(topRight.y - topLeft.y, 2));
        double verticalD = Math.sqrt(Math.pow(topMid.x - centralMid.x, 2) + Math.pow(topMid.y - centralMid.y, 2));
        if (topLeft.x - horizontalD <= -1) {
            nextCube = new Cube(
                    new Point2D.Double(topRight.x, topRight.y + verticalD),
                    new Point2D.Double(topMid.x, topMid.y),
                    new Point2D.Double(topMid.x + horizontalD, topMid.y),
                    new Point2D.Double(topRight.x, topRight.y)
            );
        } else if (topRight.x + horizontalD >= 1) {
            nextCube = new Cube(
                    new Point2D.Double(topLeft.x, topLeft.y + verticalD),
                    new Point2D.Double(topMid.x - horizontalD, topMid.y),
                    new Point2D.Double(topMid.x, topMid.y),
                    new Point2D.Double(topLeft.x, topLeft.y)
            );
        } else if (randomNumber < 10) {
            nextCube = new Cube(
                    new Point2D.Double(topRight.x, topRight.y + verticalD),
                    new Point2D.Double(topMid.x, topMid.y),
                    new Point2D.Double(topMid.x + horizontalD, topMid.y),
                    new Point2D.Double(topRight.x, topRight.y)
            );
        } else {
            nextCube = new Cube(
                    new Point2D.Double(topLeft.x, topLeft.y + verticalD),
                    new Point2D.Double(topMid.x - horizontalD, topMid.y),
                    new Point2D.Double(topMid.x, topMid.y),
                    new Point2D.Double(topLeft.x, topLeft.y)
            );
        }

    }

    public void animateCube() {
        topMid.y -= 0.001;
        topLeft.y -= 0.001;
        centralMid.y -= 0.001;
        topRight.y -= 0.001;
        botMid.y -= 0.001;
        botLeft.y -= 0.001;
        botRight.y -= 0.001;
    }

}
