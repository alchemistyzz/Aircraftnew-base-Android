package edu.hitsz.thread;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.FrozeProp;

public class FrozeThread extends Thread {
    private HeroAircraft heroAircraft;
    private FrozeProp frozeProp;

    public FrozeThread(HeroAircraft heroAircraft, FrozeProp frozeProp) {
        this.heroAircraft = heroAircraft;
        this.frozeProp = frozeProp;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            frozeProp.refresh();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
