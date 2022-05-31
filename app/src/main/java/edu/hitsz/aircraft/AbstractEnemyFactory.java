package edu.hitsz.aircraft;


/**
 * @author night
 */
public interface AbstractEnemyFactory{

    AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp);
}