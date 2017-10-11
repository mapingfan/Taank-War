package TankWar;

import java.awt.*;

public class Wall {
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    public static final Color WALLCOLOR = Color.gray;

    public Wall(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        Color temp = g.getColor();
        g.setColor(WALLCOLOR);
        g.fillRect(xPosition,yPosition,width,height);
        g.setColor(temp);
    }


    public Rectangle getRect() {
        return new Rectangle(xPosition,yPosition,this.width,this.height);
    }
}
