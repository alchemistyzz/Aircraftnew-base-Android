package edu.hitsz.aircraft;

import java.util.List;

import edu.hitsz.LaunchActivity;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.AbstractBullet;

/**
 * @author night
 */
public class MissileEnemy extends AbstractAircraft {
    public MissileEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        System.out.println("MissileSpeedX:" + speedX);
        System.out.println("MissileSpeedY:" + speedY);
    }

    @Override
    public List<AbstractBullet> shoot() {
        return null;
    }

    @Override
    public void die() {
        this.vanish();
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= LaunchActivity.WINDOW_HEIGHT) {
            vanish();
        }
    }
}
