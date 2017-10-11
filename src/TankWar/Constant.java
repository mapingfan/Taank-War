package TankWar;
/**
 * 封装 一些常量，原本打算把 程序的常量全部提取出来；
 * 后来又感觉有违面向对象的原则，合适的成员出现在合适的类中；
 * 但是我感觉把所有的吃常量封装到一个类中，可以起到配置文件的作用；
 */

import java.util.Random;

public class Constant {
    public static final int WINDOWHEIGHT = 600;
    public static final int WINDOWWIDTH = 800;
    public static final Random random = new Random();
    public static final Direction[] directions = Direction.values();
}
