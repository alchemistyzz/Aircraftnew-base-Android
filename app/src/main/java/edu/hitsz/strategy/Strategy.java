package edu.hitsz.strategy;

import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

/**
 * @author night
 */
public interface Strategy {
    List<AbstractBullet> shoot(AbstractAircraft aircraft,int power,int shootNum,int direction);
}
