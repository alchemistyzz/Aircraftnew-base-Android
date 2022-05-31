package edu.hitsz.strategy;

import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

public class ContextShoot {
    private Strategy strategy;
    public ContextShoot(Strategy strategy){
        this.strategy=strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<AbstractBullet> executeStrategy(AbstractAircraft aircraft, int power, int shootNum, int direction){
        return strategy.shoot(aircraft,power,shootNum,direction);
    }
}
