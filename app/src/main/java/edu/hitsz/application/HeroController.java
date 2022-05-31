package edu.hitsz.application;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import edu.hitsz.aircraft.HeroAircraft;


/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroController  {
    private Game game;
    private HeroAircraft heroAircraft;


    public HeroController(Game game, HeroAircraft heroAircraft) {
        this.game = game;
        this.heroAircraft = heroAircraft;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        System.out.println(111);
//        super.onCreate(savedInstanceState);
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println(111);
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            float ex = event.getX();
//            float ey = event.getY();
//            heroAircraft.setLocation(ex,ey);
//            return true;
//        }
//        return false;
//    }

}
