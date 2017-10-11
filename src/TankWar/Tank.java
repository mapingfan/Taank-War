package TankWar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import static TankWar.Constant.WINDOWHEIGHT;
import static TankWar.Constant.WINDOWWIDTH;

public class Tank {

    public static final Color GOODTANKCOLOR = Color.RED;
    public static final Color BADTANKCOLOR = Color.BLUE;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private Direction tankDirection = Direction.STILL;
    private Direction barrelDirection = Direction.UP; //这个方向代表炮筒的方向，默认向上；
    public static final int TANKSPEED = 5;
    public static final int TANKSIZE = 50; //绘制圆代表坦克；
    private int xPosition;
    private int yPosition;
    private int oldXPosition;
    private int oldYPosition;
    TankWindow tankWindow;
    private boolean isGoodTank;
    private boolean isLive = true;
    private int step = 0;
    private int life = 100;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Tank(int xPosition, int yPosition, TankWindow tankWindow, boolean isGoodTank, boolean isLive) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        oldXPosition = xPosition;
        oldYPosition = yPosition;
        this.tankWindow = tankWindow;
        this.isGoodTank = isGoodTank;
        this.isLive = isLive;
    }

    public Tank(int xPosition, int yPosition, TankWindow tankWindow, boolean isGoodTank, boolean isLive, Direction direction) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.tankWindow = tankWindow;
        this.isGoodTank = isGoodTank;
        this.isLive = isLive;
        this.tankDirection = direction;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isLive() {
        return isLive;
    }

    public void draw(Graphics g) {
        if (!isLive) {
            return;
        }
        Color temp = g.getColor();
        if (isGoodTank) {
            g.setColor(GOODTANKCOLOR);
        } else {
            g.setColor(BADTANKCOLOR);
        }
        if (isGoodTank) {
            g.drawRect(xPosition, yPosition - 20, TANKSIZE, 18);
            g.fillRect(xPosition, yPosition - 20, getLife()*TANKSIZE/100, 18);
        }
        g.fillOval(xPosition, yPosition, TANKSIZE, TANKSIZE);
        g.setColor(Color.BLACK);
        switch (barrelDirection) {
            case LEFT:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition, yPosition + TANKSIZE / 2);
                break;
            case LEFTUP:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition, yPosition);
                break;
            case UP:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition + TANKSIZE / 2, yPosition);
                break;
            case RIGHTUP:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition + TANKSIZE, yPosition);
                break;
            case RIGHT:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition + TANKSIZE, yPosition + TANKSIZE / 2);
                break;
            case RIGHTDOWN:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition + TANKSIZE, yPosition + TANKSIZE);
                break;
            case DOWN:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition + TANKSIZE / 2, yPosition + TANKSIZE);
                break;
            case LEFTDOWN:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition, yPosition + TANKSIZE);
                break;
            default:
                g.drawLine(xPosition + TANKSIZE / 2, yPosition + TANKSIZE / 2, xPosition + TANKSIZE / 2, yPosition);
                break;
        }
        g.setColor(temp);
        move();
    }

    public void move() {
        oldYPosition = yPosition;
        oldXPosition = xPosition;
        switch (tankDirection) {
            case LEFT:
                xPosition -= TANKSPEED;
                break;
            case LEFTUP:
                xPosition -= TANKSPEED;
                yPosition -= TANKSPEED;
                break;
            case UP:
                yPosition -= TANKSPEED;
                break;
            case RIGHTUP:
                xPosition += TANKSPEED;
                yPosition -= TANKSPEED;
                break;
            case RIGHT:
                xPosition += TANKSPEED;
                break;
            case RIGHTDOWN:
                xPosition += TANKSPEED;
                yPosition += TANKSPEED;
                break;
            case DOWN:
                yPosition += TANKSPEED;
                break;
            case LEFTDOWN:
                xPosition -= TANKSPEED;
                yPosition += TANKSPEED;
                break;
            case STILL:
            default:
                break;
        }
      /*
       *需要检测坦克运动是否出界；
       */
        if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition < 26) {
            yPosition = 26;
        }
        if (xPosition > WINDOWWIDTH - Tank.TANKSIZE) {
            xPosition = WINDOWWIDTH - Tank.TANKSIZE;
        }
        if (yPosition > WINDOWHEIGHT - TANKSIZE) {
            yPosition = WINDOWHEIGHT - TANKSIZE;
        }
        if (hitWall(tankWindow.getWall1()) || hitWall(tankWindow.getWall2())) {
            stay();
        }

    }

    private void stay() {
        xPosition = oldXPosition;
        yPosition = oldYPosition;
    }

    public void randomChangeDirection() {
        if (step == Constant.directions.length) {
            step = 0;
        }
        if (Constant.random.nextInt(100) > 97) {
            barrelDirection = tankDirection = Constant.directions[step];
            if (!isGoodTank) {
                this.fire();
            }
        }
        step++;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                isLeftPressed = true;
                break;
            case KeyEvent.VK_UP:
                isUpPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                isRightPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                isDownPressed = true;
                break;
            case KeyEvent.VK_Q:
                //tankWindow.getBulletArrayList().add(fire());
                fire();
                break;
            case KeyEvent.VK_W:
                superFire();
                break;
            case KeyEvent.VK_F2:
                if (!isLive) {
                    isLive = true;
                    this.life = 100;
                }

        }
        /*
         *根据按键产生的布尔值，然后根据布尔值确定坦克的方向；
         * 坦克的移动按照自己的方向来；
         */
        if (isLeftPressed && !isUpPressed && !isRightPressed && !isDownPressed) {
            barrelDirection = tankDirection = Direction.LEFT;
        } else if (isLeftPressed && isUpPressed && !isRightPressed && !isDownPressed) {
            barrelDirection = tankDirection = Direction.LEFTUP;
        } else if (!isLeftPressed && isUpPressed && !isRightPressed && !isDownPressed) {
            barrelDirection = tankDirection = Direction.UP;
        } else if (!isLeftPressed && isUpPressed && isRightPressed && !isDownPressed) {
            barrelDirection = tankDirection = Direction.RIGHTUP;
        } else if (!isLeftPressed && !isUpPressed && isRightPressed && !isDownPressed) {
            barrelDirection = tankDirection = Direction.RIGHT;
        } else if (!isLeftPressed && !isUpPressed && isRightPressed && isDownPressed) {
            barrelDirection = tankDirection = Direction.RIGHTDOWN;
        } else if (!isLeftPressed && !isUpPressed && !isRightPressed && isDownPressed) {
            barrelDirection = tankDirection = Direction.DOWN;
        } else if (isLeftPressed && !isUpPressed && !isRightPressed && isDownPressed) {
            barrelDirection = tankDirection = Direction.LEFTDOWN;
        } else {
            tankDirection = Direction.STILL;

        }
    }

    private void superFire() {
        if (isLive) {
            for (int i = 0; i < 8; i++) {
                tankWindow.getBulletArrayList().
                        add(new Bullet
                                (xPosition + TANKSIZE / 2 - Bullet.BULLETSIZE / 2, yPosition + TANKSIZE / 2 - Bullet.BULLETSIZE / 2, Constant.directions[i], tankWindow, this.isGoodTank));
            }

        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                isLeftPressed = false;
                break;
            case KeyEvent.VK_UP:
                isUpPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                isRightPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                isDownPressed = false;
                break;
        }

        tankDirection = Direction.STILL;
    }

    public void fire() {
        if (isLive) {
            tankWindow.getBulletArrayList().add(new Bullet(xPosition + TANKSIZE / 2 - Bullet.BULLETSIZE / 2, yPosition + TANKSIZE / 2 - Bullet.BULLETSIZE / 2, barrelDirection, tankWindow, this.isGoodTank));
        }
    }

    public Rectangle getRect() {
        return new Rectangle(xPosition, yPosition, TANKSIZE, TANKSIZE);
    }

    public boolean isGoodTank() {
        return isGoodTank;
    }

    public boolean hitWall(Wall wall) {
        if (this.isLive && this.getRect().intersects(wall.getRect())) {
            return true;
        }
        return false;

    }

    public boolean collapseWithTank(Tank tank) {
        if (this != tank && this.isLive && tank.isLive && this.getRect().intersects(tank.getRect())) {
            this.stay();
            tank.stay();
            return true;
        }
        return false;
    }

    public boolean collapseWithTanks(ArrayList<Tank> tanks) {
        for (Iterator<Tank> iterator = tanks.iterator(); iterator.hasNext(); ) {
            Tank tank = iterator.next();
            if (this.collapseWithTank(tank)) {
                return true;
            }
        }
        return false;
    }

    public void addBlood(Blood blood) {
        if (this.isLive&&this.isGoodTank&&blood.isLive()&&this.getRect().intersects(blood.getRect())) {
            blood.setLive(false);
            this.life = 100;
        }
    }
}
