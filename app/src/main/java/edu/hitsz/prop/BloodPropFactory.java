package edu.hitsz.prop;

public class BloodPropFactory implements AbstractPropFactory{

    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new BloodProp(locationX,locationY,speedX,speedY,power);
    }
}
