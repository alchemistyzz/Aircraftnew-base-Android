package edu.hitsz.prop;

import java.util.ArrayList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

public class FrozeProp extends AbstractProp{
    private List<AbstractAircraft> abstractAircrafts=new ArrayList<>();
    private List<AbstractBullet> abstractBullets=new ArrayList<>();
    public FrozeProp(int locationX, int locationY, int speedX, int speedY, int power) {
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

    public  void Froze(){
        for(AbstractAircraft abstractAircraft:abstractAircrafts){
            abstractAircraft.setSpeedX(abstractAircraft.getSpeedX()/2);
            abstractAircraft.setSpeedY(abstractAircraft.getSpeedY()/2);
        }
        for(AbstractBullet abstractBullet:abstractBullets){
            abstractBullet.setSpeedX(abstractBullet.getSpeedX()/2);
            abstractBullet.setSpeedY(abstractBullet.getSpeedY()/2);
        }
    }

    public void refresh(){
        for(AbstractAircraft abstractAircraft:abstractAircrafts){
            abstractAircraft.setSpeedX(abstractAircraft.getSpeedX()*2);
            abstractAircraft.setSpeedY(abstractAircraft.getSpeedY()*2);
        }
        for(AbstractBullet abstractBullet:abstractBullets){
            abstractBullet.setSpeedX(abstractBullet.getSpeedX()*2);
            abstractBullet.setSpeedY(abstractBullet.getSpeedY()*2);
        }
    }


}
