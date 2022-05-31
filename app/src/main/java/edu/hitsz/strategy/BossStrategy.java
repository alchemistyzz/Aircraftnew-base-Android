package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;

public class BossStrategy implements Strategy {
    @Override
    public List<AbstractBullet> shoot(AbstractAircraft aircraft, int power, int shootNum, int direction) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * 4;
        AbstractBullet abstractBullet;
        if (shootNum == 0) {
            for (int i = 0; i < 7; i++) {
                res.add(addBullet(-8, i, aircraft));
            }
            res.add(addBullet(-9, 7, aircraft));
            res.add(addBullet(-9, 8, aircraft));
            res.add(addBullet(-10, 8, aircraft));
            res.add(addBullet(-11, 8, aircraft));
            res.add(addBullet(-12, 7, aircraft));
            res.add(addBullet(-12, 6, aircraft));
            res.add(addBullet(-12, 5, aircraft));

            for (int i = 0; i < 3; i++) {
                res.add(addBullet(-1,6+i,aircraft));
            }
            for(int i=0;i<4;i++){
                res.add(addBullet(-2,3+i,aircraft));
            }
            for(int i=0;i<3;i++){
                res.add(addBullet(-3,i,aircraft));
            }
            for(int i=0;i<5;i++){
                res.add(addBullet(-4,1+i,aircraft));
            }
            for(int i=0;i<5;i++){
                res.add(addBullet(-5,4+i,aircraft));
            }
            res.add(addBullet(-3,5,aircraft));
            res.add(addBullet(-6,8,aircraft));

            res.add(addBullet(1,0,aircraft));
            for(int i=0;i<4;i++){
                res.add(addBullet(2,1+i,aircraft));
            }
            for(int i=0;i<4;i++){
                res.add(addBullet(3,4+i,aircraft));
            }
            for(int i=0;i<3;i++){
                res.add(addBullet(4,6+i,aircraft));
            }
            for(int i=0;i<4;i++){
                res.add(addBullet(5,2+i,aircraft));
            }
            for(int i=0;i<3;i++){
                res.add(addBullet(6,i,aircraft));
            }

            for (int i = 0; i < 3; i++) {
                res.add(addBullet(13,6+i,aircraft));
            }
            for(int i=0;i<4;i++){
                res.add(addBullet(12,3+i,aircraft));
            }
            for(int i=0;i<3;i++){
                res.add(addBullet(11,i,aircraft));
            }
            for(int i=0;i<5;i++){
                res.add(addBullet(10,1+i,aircraft));
            }
            for(int i=0;i<5;i++){
                res.add(addBullet(9,4+i,aircraft));
            }
            res.add(addBullet(11,5,aircraft));
            res.add(addBullet(8,8,aircraft));

        } else if (shootNum == 1) {
            for (int i = 0; i < 8; i++) {
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                abstractBullet = new EnemyBullet(x + (i * 2 - 6 + 1) * 10, y, speedX, speedY, power);
                res.add(abstractBullet);
            }
        } else if (shootNum == 2) {
            for (int i = 0; i <= 6; i++) {
                for (int j = 0; j < i; j++) {
                    abstractBullet = new EnemyBullet(x + (j * 2 - i + 1) * 10, y + i * 15, speedX, speedY, power);
                    res.add(abstractBullet);
                }
            }
            for (int i = 0; i <= 6; i++) {
                for (int j = 0; j < 6 - i; j++) {
                    abstractBullet = new EnemyBullet(x + (j * 2 - (6 - i) + 1) * 10, y + (i + 6) * 15, speedX, speedY, power);
                    res.add(abstractBullet);
                }
            }
        } else if (shootNum == 3) {
            for (int i = 0; i < 6; i++) {
                // 子弹发射位置相对飞机位置向前偏移
                // 多个子弹横向分散
                abstractBullet = new EnemyBullet(x + (i * 2 - 6 + 1) * 10, y, speedX + (i * 2 - 6 + 1), speedY, power);
                res.add(abstractBullet);
            }

        }

        return res;
    }

    private EnemyBullet addBullet(int posx, int posy, AbstractAircraft aircraft) {
        int direction = 1;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * 4;
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        return new EnemyBullet(x + posx * 10, y + posy * 10, speedX, speedY, 30);
    }

}
