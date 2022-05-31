package edu.hitsz.prop;

public interface AbstractPropFactory {
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int power);
}
