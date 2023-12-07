package Model;

import javax.media.opengl.GL;
import java.awt.geom.Point2D;

public class Diamond {
    public Point2D.Double bottomLeft, bottomRight, topRight, topLeft , center;

    public Diamond(
            Point2D.Double bottomLeft,
            Point2D.Double bottomRight,
            Point2D.Double topRight,
            Point2D.Double topLeft
    ) {
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.center = new Point2D.Double((bottomRight.x + bottomLeft.x) / 2, (topLeft.y + bottomLeft.y) / 2);
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

    public void animateDiamond() {
        bottomLeft.y -= 0.001;
        topLeft.y -= 0.001;
        bottomRight.y -= 0.001;
        topRight.y -= 0.001;
    }
}
