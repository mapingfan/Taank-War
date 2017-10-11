package TankWar;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import static TankWar.Constant.*;

/**
 * TankClient类用于主程序类，负责启动整个程序；
 * TankWindow类表示整个游戏的窗口；
 * @author 马平凡
 *
 *
 */
public class TankClient {

    public static void main(String[] args) {
        TankWindow tankWindow = new TankWindow();
        tankWindow.launchFrame();
    }
}

class TankWindow extends Frame {

    public static final int STARTXPOSITION = 200; //窗口的起始坐标
    public static final int STARTYPOSITION = 200;//窗口的起始坐标
    public static final Color WINDOWBGCOLOR = Color.GREEN;//背景色
    private Tank tank = new Tank(50,50,this,true,true);
    /**
     * 用于双缓冲的静态常量；
     */
    private Image imageBuffer;
    private Graphics graphicsBuffer;
    //存放子弹
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    //存放敌人的坦克
    private ArrayList<Tank> enemyTanks = new ArrayList<>();
    private ArrayList<Explosion> explosionArrayList = new ArrayList<>();
    private Wall wall1 = new Wall(200,200,20,300);
    private Wall wall2 = new Wall(400,300,300,20);
    private Blood blood = new Blood();

    public Wall getWall1() {
        return wall1;
    }

    public Wall getWall2() {
        return wall2;
    }

    private void addEnemyTank() {
        for (int i = 0; i <Integer.parseInt(Property.getPropertyValue("enemytanks")); i++) {
            enemyTanks.add(new Tank(Constant.random.nextInt(Constant.WINDOWWIDTH),Constant.random.nextInt(Constant.WINDOWHEIGHT),this,false,true,directions[Constant.random.nextInt(directions.length)]));
        }
    }

    public ArrayList<Bullet> getBulletArrayList() {
        return bulletArrayList;
    }

    public ArrayList<Explosion> getExplosionArrayList() {
        return explosionArrayList;
    }

    private void manageWindow() {
        //set the position of the window in the screen ;
        this.setBounds(STARTXPOSITION,STARTYPOSITION,WINDOWWIDTH,WINDOWHEIGHT);
        //设置窗口大小不可以调整;
        this.setResizable(false);
        this.setBackground(WINDOWBGCOLOR);
        this.addKeyListener(new KeyMonitor());
        /*
         *添加窗口响应事件；
         * 使用匿名类实现；
         */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    public void launchFrame() {
        manageWindow();
        addEnemyTank();
        Thread thread = new Thread(new RepaintThread());
        thread.start();
    }
    @Override
    public void paint(Graphics g) {
        blood.draw(g);
        wall1.draw(g);
        wall2.draw(g);
        Color temp = g.getColor();
        g.setColor(Color.magenta);
        g.drawString("Bullet Count: "+bulletArrayList.size(),20,40);
        g.drawString("Tank Count : "+enemyTanks.size(),20,60);
        g.drawString("Life : "+tank.getLife(),20,80);
        g.setColor(temp);
        tank.draw(g);
        tank.addBlood(blood);
        if (enemyTanks.size() == 0) {
            addEnemyTank();
        }

        for (Iterator<Tank> iterator = enemyTanks.iterator(); iterator.hasNext(); ) {
            Tank enemyTank = iterator.next();
            if (enemyTank.isLive()) {
                enemyTank.randomChangeDirection();
                enemyTank.collapseWithTanks(enemyTanks);
                enemyTank.collapseWithTank(tank);
                enemyTank.draw(g);
            }
            else {
                iterator.remove();
            }
        }

        for (Iterator<Bullet> iterator = bulletArrayList.iterator(); iterator.hasNext(); ) {
            Bullet bullet =  iterator.next();
            if (bullet.isIslive()) {
                bullet.draw(g);
                bullet.hitTanks(enemyTanks);
                bullet.hitTank(tank);
                bullet.hitWall(wall1);
                bullet.hitWall(wall2);
            } else {
                iterator.remove();
            }
        }

        for (Iterator<Explosion> iterator = explosionArrayList.iterator(); iterator.hasNext(); ) {
            Explosion explosion = iterator.next();
            if (explosion.isLive()) {
                explosion.draw(g);
            } else {
                iterator.remove();
            }


        }
    }

    /*
     *重写update函数，实现双缓冲机制；
     * repaint函数的执行过程：调用update() --> 然后调用paint();
     * 两种实现双缓冲的机制：基于update函数或者基于paint函数；
     */
    @Override
    public void update(Graphics g) {
        if (imageBuffer == null) { //imageBuffer ,第一个缓冲
            imageBuffer = this.createImage(WINDOWWIDTH,WINDOWHEIGHT);
        }
        graphicsBuffer = imageBuffer.getGraphics(); //graphicsBuffer,第二个缓冲
        graphicsBuffer.setColor(WINDOWBGCOLOR);
        graphicsBuffer.fillRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        paint(graphicsBuffer);
        g.drawImage(imageBuffer,0,0,this);
    }

    /*
             *使用静态内部类实现坦克重绘。
             */
    private class RepaintThread implements Runnable {
        /*
         *每隔30ms,重绘一次。
         */
        @Override
        public void run() {
            //遇见的第一个BUG：忘记在使用while循环，使得repaint函数一直执行。
            while (true) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();
            }
        }
    }

    /*
     *这个类用于监听键盘事件；
     * 注意理解为什么继承WindowAdapter，而不是是实现KeyListener接口；
     *
     */
    private class KeyMonitor extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            tank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            tank.keyReleased(e);
        }
    }
}


