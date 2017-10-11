package TankWar;

import java.awt.*;

public class Blood {
    int[][] points = {{200,300}, {88,120}, {40,90}, {100,150}, {300,340}, {400,280}, {100,555}, {323,534}};
    int cnt = 0;
    int step = 1;
    int xPosition = 200;
    int yPosition = 200;
    public static final int BLOODSIZE = 15;
    private boolean isLive = true;
    private int time = 0;

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Rectangle getRect() {
        return new Rectangle(xPosition,yPosition,BLOODSIZE,BLOODSIZE);
    }

    private void move() {
        if (time < 1000) {
            if (cnt == points.length - 1) {
                cnt = 0;
            }
            if (step > 100) {
                xPosition = points[3][0];
                yPosition = points[3][1];
                cnt++;
                step = 1;
            } else {
                xPosition = points[cnt][0];
                yPosition = points[cnt][1];

            }
            step++;
            time++;
        }
       else {
            if (isLive == true) {
                isLive = false;
            } else {
                isLive = true;
            }
            time = 0;
        }
    }

    public void draw(Graphics g) {
        if (!isLive) {
            return;
        }
        Color temp = g.getColor();
        g.setColor(Color.magenta);
        g.fillRect(xPosition,yPosition,BLOODSIZE,BLOODSIZE);
        move();
        g.setColor(temp);
    }
}
