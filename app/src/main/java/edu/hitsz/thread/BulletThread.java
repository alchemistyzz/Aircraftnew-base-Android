package edu.hitsz.thread;

import edu.hitsz.aircraft.HeroAircraft;

public class BulletThread extends Thread {
    private HeroAircraft heroAircraft;

    public BulletThread(HeroAircraft heroAircraft) {
        this.heroAircraft = heroAircraft;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
            if (Thread.currentThread().isInterrupted()) {
                heroAircraft.setShootNum(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}