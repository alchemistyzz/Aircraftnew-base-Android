package edu.hitsz.aircraft;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.GameActivity;
import edu.hitsz.LaunchActivity;
import edu.hitsz.bullet.AbstractBullet;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        System.out.println("MobSpeedX:"+speedX);
        System.out.println("MobSpeedY:"+speedY);
        System.out.println("MobHp:"+hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= GameActivity.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public void die() {
        this.vanish();
    }
}
