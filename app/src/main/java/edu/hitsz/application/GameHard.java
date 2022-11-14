package edu.hitsz.application;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;

public class GameHard extends Game{


    public GameHard(Context context) {
        super(context);
    }

    @Override
    public void setDifficulty() {
        super.setHeroHp(10000);
        super.setMaxHp(10000);

        super.setBossHp(400);
        super.setBossSpeedX(10);
        super.setBossSPeedY(0);

        super.setEliteHp(50);
        super.setEliteSpeedX(5);
        super.setEliteSpeedY(5);

        super.setMobHp(25);
        super.setMobSpeedX(5);
        super.setMobSpeedY(5);

        super.setBossTarget(150);
        super.setTimeRate(20);
        super.setGenerateRate(4);
        super.setEnemyMaxNumber(20);

        super.setMissileRate(5);
        super.setMissileSpeedY(20);
        super.setBossShootRate(4);

    }

}
