package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Cube {
    public Point2D.Float topMid;
    public Point2D.Float topLeft;
    public Point2D.Float topRight;
    public Point2D.Float centralMid;
    public Point2D.Float botMid;
    public Point2D.Float botLeft;
    public Point2D.Float botRight;
    public Point2D.Float centerTileP;
    public Cube nextCube;
    public Diamond diamond;
    public String relativePos;
    public static final String RIGHT = "right";
    public static final String LEFT = "left";
    public boolean hasBeenPassed = false;

    public Cube(Point2D.Float topMid,
                Point2D.Float topLeft,
                Point2D.Float topRight,
                Point2D.Float centralMid) {

        this.topMid = topMid;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.centralMid = centralMid;
        this.botMid = new Point2D.Float(centralMid.x, centralMid.y - 0.3f);
        this.botLeft = new Point2D.Float(topLeft.x, topLeft.y - 0.3f);
        this.botRight = new Point2D.Float(topRight.x, topRight.y - 0.3f);
        this.centerTileP = new Point2D.Float((topLeft.x + topRight.x) / 2, (topLeft.y + topRight.y) / 2);
        this.relativePos = null;

        generateDiamond();
    }

    public Cube(Point2D.Float topMid,
                Point2D.Float topLeft,
                Point2D.Float topRight,
                Point2D.Float centralMid,
                String relativePos) {

        this.topMid = topMid;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.centralMid = centralMid;
        this.botMid = new Point2D.Float(centralMid.x, centralMid.y - 0.3f);
        this.botLeft = new Point2D.Float(topLeft.x, topLeft.y - 0.3f);
        this.botRight = new Point2D.Float(topRight.x, topRight.y - 0.3f);
        this.centerTileP = new Point2D.Float((topLeft.x + topRight.x) / 2, (topLeft.y + topRight.y) / 2);
        this.relativePos = relativePos;

        generateDiamond();
    }

    public void drawCube(GL gl, int texture) {
        drawTile(gl);
        drawRProjection(gl);
        drawLProjection(gl);
        if (diamond != null) {
            diamond.drawDiamond(gl, texture);
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
                    new Point2D.Float((topLeft.x + centralMid.x) / 2 + 0.03f, (topLeft.y + centralMid.y) / 2 + 0.01f),
                    new Point2D.Float((centralMid.x + topRight.x) / 2 - 0.03f, (centralMid.y + topRight.y) / 2 + 0.01f),
                    new Point2D.Float((topRight.x + topMid.x) / 2 - 0.03f, (topRight.y + topMid.y) / 2 - 0.01f),
                    new Point2D.Float((topMid.x + topLeft.x) / 2 + 0.03f, (topMid.y + topLeft.y) / 2 - 0.01f)
            );
        }

    }

    public void generateNewCube() {
        int randomNumber = (int) (Math.random() * 20);
        float horizontalD = (float) Math.sqrt(Math.pow(topRight.x - topLeft.x, 2) + Math.pow(topRight.y - topLeft.y, 2));
        float verticalD = (float) Math.sqrt(Math.pow(topMid.x - centralMid.x, 2) + Math.pow(topMid.y - centralMid.y, 2));

        if (topLeft.x - horizontalD <= -1) {
            nextCube = new Cube(
                    new Point2D.Float(topRight.x, topRight.y + verticalD),
                    new Point2D.Float(topMid.x, topMid.y),
                    new Point2D.Float(topMid.x + horizontalD, topMid.y),
                    new Point2D.Float(topRight.x, topRight.y),
                    RIGHT
            );
        } else if (topRight.x + horizontalD >= 1) {
            nextCube = new Cube(
                    new Point2D.Float(topLeft.x, topLeft.y + verticalD),
                    new Point2D.Float(topMid.x - horizontalD, topMid.y),
                    new Point2D.Float(topMid.x, topMid.y),
                    new Point2D.Float(topLeft.x, topLeft.y),
                    LEFT
            );
        } else if (randomNumber < 10) {
            nextCube = new Cube(
                    new Point2D.Float(topRight.x, topRight.y + verticalD),
                    new Point2D.Float(topMid.x, topMid.y),
                    new Point2D.Float(topMid.x + horizontalD, topMid.y),
                    new Point2D.Float(topRight.x, topRight.y),
                    RIGHT
            );
        } else {
            nextCube = new Cube(
                    new Point2D.Float(topLeft.x, topLeft.y + verticalD),
                    new Point2D.Float(topMid.x - horizontalD, topMid.y),
                    new Point2D.Float(topMid.x, topMid.y),
                    new Point2D.Float(topLeft.x, topLeft.y),
                    LEFT
            );
        }
    }

    public void animateCube(float speed) {
        topMid.y -= speed;
        topLeft.y -= speed;
        centralMid.y -= speed;
        topRight.y -= speed;
        botMid.y -= speed;
        botLeft.y -= speed;
        botRight.y -= speed;
        centerTileP.y -= speed;
    }

    public void animateFallingCube(GL gl, int texture, float speed) {
        drawCube(gl, texture);
        topMid.y -= speed + 0.002f;
        topLeft.y -= speed + 0.002f;
        centralMid.y -= speed + 0.002f;
        topRight.y -= speed + 0.002f;
        botMid.y -= speed + 0.002f;
        botLeft.y -= speed + 0.002f;
        botRight.y -= speed + 0.002f;
        centerTileP.y -= speed + 0.002f;
    }

    public boolean isInside(Point2D.Float point) {
        float x1, x2;

        if (point.y < centerTileP.y) {
            x1 = Math.round((-centralMid.y + centralMid.x) * 100) / 100.0f; // right point
            x2 = Math.round((centralMid.y + centralMid.x) * 100) / 100.0f; // left point
        } else {
            x1 = Math.round((topRight.y + topRight.x) * 100) / 100.0f; // right point
            x2 = Math.round((-topLeft.y + topLeft.x) * 100) / 100.0f; // left point
        }

        return Math.round(point.x * 100) / 100.0f >= x2 && Math.round(point.x * 100) / 100.0f <= x1;
    }
}