package edu.hitsz.aircraft;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.HeroBullet;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = -1;


    private boolean superState = false;


    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */

    private volatile static HeroAircraft instance = null;



    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public static synchronized HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<AbstractBullet> shoot() {
        List<AbstractBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 5;
        AbstractBullet abstractBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            abstractBullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            res.add(abstractBullet);
        }
        return res;
    }

    public void setShootNum(int shootNum) {
        this.shootNum =shootNum;
    }

    public int getShootNum() {
        return shootNum;
    }


    public void refresh(int locationX, int locationY, int speedX, int speedY, int hp){
        this.locationX=locationX;
        this.locationY=locationY;
        this.speedX=speedX;
        this.speedY=speedY;
        this.hp=hp;
        this.isValid=true;
        this.shootNum=1;
    }
    public boolean getSuperState() {
        return superState;
    }

    public void setSuperState(boolean superState) {
        this.superState = superState;
    }


    @Override
    public void die() {

    }
}
