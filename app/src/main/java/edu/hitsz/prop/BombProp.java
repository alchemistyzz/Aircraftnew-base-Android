package edu.hitsz.prop;

import java.util.ArrayList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

/**
 * @Author hitsz
 */
public class BombProp extends AbstractProp {

    private List<AbstractAircraft> abstractAircrafts=new ArrayList<>();
    private List<AbstractBullet> abstractBullets=new ArrayList<>();

    public BombProp(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    public void addEnemy(AbstractAircraft abstractAircraft){
        abstractAircrafts.add(abstractAircraft);
    }

    public void addBullet(AbstractBullet abstractBullet){
        abstractBullets.add(abstractBullet);
    }

    public void deleteEnemy(AbstractAircraft abstractAircraft){
        abstractAircrafts.remove(abstractAircraft);
    }

    public  void bomb(){
        for(AbstractAircraft abstractAircraft:abstractAircrafts){
            abstractAircraft.die();
        }
        for(AbstractBullet abstractBullet:abstractBullets){
            abstractBullet.die();
        }
    }


}
