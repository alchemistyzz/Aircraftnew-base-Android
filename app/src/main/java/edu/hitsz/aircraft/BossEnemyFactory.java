package edu.hitsz.aircraft;

public class BossEnemyFactory implements AbstractEnemyFactory{

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new BossEnemy(locationX,locationY,speedX,speedY,hp);
    }
}
