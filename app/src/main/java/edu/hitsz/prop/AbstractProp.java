package edu.hitsz.prop;

import edu.hitsz.GameActivity;
import edu.hitsz.LaunchActivity;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
public class AbstractProp extends AbstractFlyingObject {

    private int power = 10;

    public AbstractProp(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= GameActivity.WINDOW_WIDTH) {
//            this.speedX=-this.speedX;
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= GameActivity.WINDOW_HEIGHT ) {
            // 向下飞行出界
            this.speedY=-this.speedY;
        }else if (locationY <= 0){
            // 向上飞行出界
            this.speedY=-this.speedY;
        }
    }

    public int getPower() {
        return power;
    }
}
