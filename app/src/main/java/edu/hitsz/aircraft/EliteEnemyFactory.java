package edu.hitsz.aircraft;

public class EliteEnemyFactory implements AbstractEnemyFactory{

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new EliteEnemy(locationX,locationY,speedX,speedY,hp);
    }
}
