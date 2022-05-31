package edu.hitsz.prop;

public class BulletPropFactory implements AbstractPropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new BulletProp(locationX,locationY,speedX,speedY,power);
    }
}
