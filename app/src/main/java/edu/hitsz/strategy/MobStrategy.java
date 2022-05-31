package edu.hitsz.strategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

/**
 * @author night
 */
public class MobStrategy implements Strategy {
    @Override
    public List<AbstractBullet> shoot(AbstractAircraft aircraft,int power,int shootNum,int direction) {
        return new LinkedList<> ();
    }
}
