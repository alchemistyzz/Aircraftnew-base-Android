package edu.hitsz.prop;

public class BombPropFactory implements AbstractPropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new BombProp(locationX,locationY,speedX,speedY,power);
    }
}
