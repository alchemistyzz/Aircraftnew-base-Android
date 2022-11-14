package edu.hitsz.application;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;

public class GameNormal extends Game {


    public GameNormal(Context context) {
        super(context);
    }

    @Override
    public void setDifficulty() {
        super.setHeroHp(10000);
        super.setMaxHp(10000);

        super.setBossHp(200);
        super.setBossSpeedX(5);
        super.setBossSPeedY(0);

        super.setEliteHp(50);
        super.setEliteSpeedX(3);
        super.setEliteSpeedY(5);

        super.setMobHp(25);
        super.setMobSpeedX(2);
        super.setMobSpeedY(5);

        super.setBossTarget(200);
        super.setTimeRate(5);
        super.setGenerateRate(5);
        super.setEnemyMaxNumber(10);

        super.setMissileRate(10);
        super.setMissileSpeedY(10);
        super.setBossShootRate(10);

    }

}
