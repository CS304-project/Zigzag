package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Ball {
    public Point2D.Float bottomLeft, bottomRight, topRight, topLeft, center;
    public boolean isMovingRight = true;
    public float radius;
    public boolean isFalling = false;
    private boolean xFlag = false;
    private int xCounter = 0;

    public Ball(
            Point2D.Float bottomLeft,
            Point2D.Float bottomRight,
            Point2D.Float topRight,
            Point2D.Float topLeft
    ) {
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.center = new Point2D.Float((topLeft.x + bottomRight.x) / 2, (topLeft.y + bottomRight.y) / 2);
        this.radius = (float) Math.sqrt((center.x - topRight.x) * (center.x - topRight.x));
    }

    public void draw(GL gl, int texture) {
        gl.glColor3f(1, 1, 1);
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2d(bottomLeft.x, bottomLeft.y);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2d(bottomRight.x, bottomRight.y);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2d(topRight.x, topRight.y);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2d(topLeft.x, topLeft.y);
        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);
    }

    public void navigate(float speed) {
        if (isMovingRight) {
            bottomLeft.x += speed;
            bottomRight.x += speed;
            topLeft.x += speed;
            topRight.x += speed;
            center.x += speed;
        } else {
            bottomLeft.x -= speed;
            bottomRight.x -= speed;
            topLeft.x -= speed;
            topRight.x -= speed;
            center.x -= speed;
        }
    }

    public void animateFalling() {
        if (isMovingRight && !xFlag) {
            bottomLeft.x += 0.002f;
            bottomRight.x += 0.002f;
            topLeft.x += 0.002f;
            topRight.x += 0.002f;
            center.x += 0.002f;
            xCounter++;
        } else if (!isMovingRight && !xFlag) {
            bottomLeft.x -= 0.002f;
            bottomRight.x -= 0.002f;
            topLeft.x -= 0.002f;
            topRight.x -= 0.002f;
            center.x -= 0.002f;
            xCounter++;
        }
        if (xCounter > 50) xFlag = true;
        if (xFlag) {
            bottomLeft.y -= 0.006f;
            bottomRight.y -= 0.006f;
            topLeft.y -= 0.006f;
            topRight.y -= 0.006f;
        }
    }
}