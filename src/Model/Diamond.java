package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Diamond {
    public Point2D.Float bottomLeft, bottomRight, topRight, topLeft , center;
    public  float radius ;
    public Diamond(
            Point2D.Float bottomLeft,
            Point2D.Float bottomRight,
            Point2D.Float topRight,
            Point2D.Float topLeft
    ) {
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.center = new Point2D.Float((bottomRight.x + bottomLeft.x) / 2, (topLeft.y + bottomLeft.y) / 2);
        this.radius = (float) Math.sqrt((center.x-topRight.x)*(center.x-topRight.x));
    }

    public void drawDiamond(GL gl, int texture) {
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

    public void animateDiamond(float speed) {
        bottomLeft.y -= speed;
        topLeft.y -= speed;
        bottomRight.y -= speed;
        topRight.y -= speed;
        center.y -=speed;
    }

    public void animateFallingDiamond(float speed) {
        bottomLeft.y -= speed + 0.002f;
        topLeft.y -= speed + 0.002f;
        bottomRight.y -= speed + 0.002f;
        topRight.y -= speed + 0.002f;
        center.y -=speed + 0.002f;
    }
}
