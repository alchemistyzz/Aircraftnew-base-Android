package edu.hitsz.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Surface;

public class GameEasy extends Game{


    public GameEasy(Context context) {
        super(context);
    }

    @Override
    public void setDifficulty() {

        super.setBossHp(100);
        super.setBossSpeedX(5);
        super.setBossSPeedY(0);

        super.setEliteHp(25);
        super.setEliteSpeedX(5);
        super.setEliteSpeedY(5);

        super.setMobHp(25);
        super.setMobSpeedX(0);
        super.setMobSpeedY(5);

        super.setBossTarget(Integer.MAX_VALUE);
        super.setTimeRate(0);
        super.setGenerateRate(5);
        super.setEnemyMaxNumber(5);

        super.setMissileRate(20);
        super.setMissileSpeedY(10);
        super.setBossShootRate(1000);

    }





}
