package edu.hitsz.prop;

public class FrozePropFactory implements AbstractPropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new FrozeProp(locationX,locationY,speedX,speedY,power);
    }
}
