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
    //子弹的位置；
    private int xPosition;
    private int yPosition;
    private Direction bulletDirection;
    public static final int BULLETSPEED = 10;
    public static final Color GOODULLETCOLOR = Color.RED;
    public static final Color BADULLETCOLOR = Color.black;
    public static final int BULLETSIZE = 10;
    private boolean islive = true; //每颗子弹new出来就默认true，出界就认为false;
    private boolean isGoodBullet; //敌人坦克的子弹是坏子弹，我方坦克的子弹是好子弹；
    private TankWindow tankWindow;

    /**
     *
     * @param xPosition 子弹横坐标；
     * @param yPosition
     * @param bulletDirection 子弹方向；
     * @param tankWindow 大管家；
     * @param isGoodBullet 好坏子弹；
     *
     */
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

    /**
     *
     * @param g 画笔，绘制子弹的方法；子弹绘制自己，方法封装在子弹中比较合理；
     */
    public void draw(Graphics g) {
        if (!islive) { //子弹死了就不要绘制了；
            return;
        }
            Color temp = g.getColor();
            if (isGoodBullet){  //好坏子弹颜色不同；
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

    //碰撞检测函数；
    public Rectangle getRect() {
        return new Rectangle(xPosition,yPosition,BULLETSIZE,BULLETSIZE);
    }

    //子弹打坦克；
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
