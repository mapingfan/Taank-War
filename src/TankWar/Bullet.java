package TankWar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 这个类表示子弹；
 * 封装子弹类的属性和方法；
 *
 */
public class Bullet {

    private int xPosition;
    private int yPosition;
    private Direction bulletDirection;
    public static final int BULLETSPEED = 10;
    public static final Color GOODULLETCOLOR = Color.RED;
    public static final Color BADULLETCOLOR = Color.black;
    public static final int BULLETSIZE = 10;
    private boolean islive = true; //每颗子弹new出来就默认true，出界就认为false;
    private boolean isGoodBullet;
    private TankWindow tankWindow;

    public Bullet(int xPosition, int yPosition, Direction bulletDirection,TankWindow tankWindow, boolean isGoodBullet) {
            this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.bulletDirection = bulletDirection;
        this.tankWindow = tankWindow;
        this.isGoodBullet = isGoodBullet;
    }



    public boolean isIslive() {
        return islive;
    }

    public void draw(Graphics g) {
        if (!islive) {
            return;
        }
            Color temp = g.getColor();
            if (isGoodBullet){
                g.setColor(GOODULLETCOLOR);
            } else {
                g.setColor(BADULLETCOLOR);
            }
            g.fillOval(xPosition, yPosition, BULLETSIZE, BULLETSIZE);
            g.setColor(temp);
            move();

    }

    private void move() {
        switch (bulletDirection) {
            case LEFT:
                xPosition -= BULLETSPEED;
                break;
            case LEFTUP:
                xPosition -= BULLETSPEED;
                yPosition -= BULLETSPEED;
                break;
            case UP:
                yPosition -= BULLETSPEED;
                break;
            case RIGHTUP:
                xPosition += BULLETSPEED;
                yPosition -= BULLETSPEED;
                break;
            case RIGHT:
                xPosition += BULLETSPEED;
                break;
            case RIGHTDOWN:
                xPosition += BULLETSPEED;
                yPosition += BULLETSPEED;
                break;
            case DOWN:
                yPosition += BULLETSPEED;
                break;
            case LEFTDOWN:
                xPosition -= BULLETSPEED;
                yPosition += BULLETSPEED;
                break;
            case STILL:
            default:
                yPosition -= BULLETSPEED;
                break;
        }
        if (xPosition<0||yPosition<0||xPosition>Constant.WINDOWWIDTH||yPosition>Constant.WINDOWHEIGHT) {
            islive = false;

        }
    }

    public Rectangle getRect() {
        return new Rectangle(xPosition,yPosition,BULLETSIZE,BULLETSIZE);
    }

    public boolean hitTank(Tank tank) {
        if (tank.isGoodTank()!=this.isGoodBullet&&tank.isLive()&&this.islive&&tank.getRect().intersects(this.getRect())) {
            if (tank.isGoodTank()){
                tank.setLife(tank.getLife()-20);
                if (tank.getLife()==0) {
                    tank.setLive(false);
                    tankWindow.getExplosionArrayList().add(new Explosion(xPosition,yPosition));
                }
            }
            else {
                tank.setLive(false);
                tankWindow.getExplosionArrayList().add(new Explosion(xPosition,yPosition));
            }
            this.islive = false;
            return true;
        }
        return false;


    }

    public boolean hitWall(Wall wall) {
       if (this.islive&&wall.getRect().intersects(this.getRect())) {
           this.islive = false;
           return true;
       }
       return false;
    }

    public boolean hitTanks(ArrayList<Tank> tanks) {
        for (Iterator<Tank> iterator = tanks.iterator(); iterator.hasNext(); ) {
            Tank tank = iterator.next();
            if (hitTank(tank)) {
                return true;
            }
        }
        return false;
    }

}
