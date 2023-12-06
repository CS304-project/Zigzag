package Model;

import javax.media.opengl.GL;
import java.awt.*;

public class Cube {
    public Point topMid;
    public Point topLeft;
    public Point topRight;
    public Point centralMid;
    public Point bottomMid;
    public Point bottomLeft;
    public Point bottomRight;
    public Point centerTileP;
    boolean hasADiamond;
    Cube nextCube;

    public Cube(Point topMid,
                Point topLeft,
                Point topRight,
                Point centralMid,
                Point bottomMid,
                Point bottomLeft,
                Point bottomRight) {

        this.topMid = topMid;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.centralMid = centralMid;
        this.bottomMid = bottomMid;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public void drawCube(GL gl, Point topMid,
                         Point topLeft,
                         Point topRight,
                         Point centralMid,
                         Point bottomMid,
                         Point bottomLeft,
                         Point bottomRight) {

        // Set Points and Colors todo
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
                    new Point(topRight.x, topRight.y + verticalD),
                    topMid,
                    new Point(topMid.x + horizontalD, topMid.y),
                    topRight,
                    new Point(topRight.x, topRight.y - 4),
                    new Point(topLeft.x, topLeft.y - 4),
                    new Point(topRight.x + horizontalD, topRight.y - 4)
            );
        } else {
            nextCube = new Cube(
                    new Point(topLeft.x, topLeft.y + verticalD),
                    new Point(topMid.x - horizontalD, topMid.y),
                    topMid,
                    topLeft,
                    new Point(topLeft.x, topLeft.y - 4),
                    new Point(topMid.x - horizontalD, topMid.y - 4),
                    new Point(topMid.x, topMid.y - 4)
            );
        }
    }
}
