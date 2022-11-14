package edu.hitsz.thread;

import edu.hitsz.aircraft.HeroAircraft;

public class SuperThread extends Thread {
    private HeroAircraft heroAircraft;

    public SuperThread(HeroAircraft heroAircraft) {
        this.heroAircraft = heroAircraft;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            heroAircraft.setSuperState(false);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
