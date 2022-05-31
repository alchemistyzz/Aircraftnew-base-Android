package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;

public class EliteStrategy implements Strategy {
    @Override
    public List<AbstractBullet> shoot(AbstractAircraft aircraft, int power, int shootNum, int direction) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * 5;
        AbstractBullet abstractBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            abstractBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            res.add(abstractBullet);
        }
        return res;
    }
}

