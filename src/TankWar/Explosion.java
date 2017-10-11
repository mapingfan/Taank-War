package TankWar;

import java.awt.*;

public class Explosion {
    private int xPosition;
    private int yPosition;
    public static final Color EXPLOSIONCOLOR = Color.black;
    public final static int[] EXPLOSIONSIZE = {10,15,20,35,40,55,30,20,10};
    private boolean isLive = true;
    private int step = 0;

    public Explosion(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public boolean isLive() {
        return isLive;
    }

    public void draw(Graphics g) {
        if (step == EXPLOSIONSIZE.length) {
            step = 0;
            isLive = false;
            return;
        }
        Color temp = g.getColor();
        g.setColor(EXPLOSIONCOLOR);
        g.fillOval(xPosition,yPosition,EXPLOSIONSIZE[step],EXPLOSIONSIZE[step]);
        step++;
        g.setColor(temp);
    }
}
