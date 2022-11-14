package edu.hitsz.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Message;
import android.view.View;
import android.widget.Button;


import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.hitsz.GameActivity;
import edu.hitsz.LaunchActivity;
import edu.hitsz.R;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemyFactory;
import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MissileEnemy;
import edu.hitsz.aircraft.MobEnemyFactory;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodPropFactory;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BombPropFactory;
import edu.hitsz.prop.BulletPropFactory;
import edu.hitsz.prop.FrozeProp;
import edu.hitsz.strategy.BossStrategy;
import edu.hitsz.strategy.ContextShoot;
import edu.hitsz.strategy.EliteStrategy;
import edu.hitsz.strategy.HeroStrategyNormal;
import edu.hitsz.strategy.HeroStrategySuper;
import edu.hitsz.thread.BulletThread;
import edu.hitsz.thread.FrozeThread;
import edu.hitsz.thread.MusicThread;
import edu.hitsz.thread.SuperThread;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
@SuppressWarnings("ALL")
public abstract class Game extends View {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    public final HeroAircraft heroAircraft;

    private final EliteEnemyFactory eliteEnemyFactory;
    private final MobEnemyFactory mobEnemyFactory;
    private final BossEnemyFactory bossEnemyFactory;
    private final BloodPropFactory bloodPropFactory;
    private final BombPropFactory bombPropFactory;
    private final BulletPropFactory bulletPropFactory;
    private final ContextShoot contextShoot;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<AbstractBullet> heroBullets;
    private final List<AbstractBullet> enemyBullets;
    private final List<AbstractProp> props;


    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;
    private int bossScore = 0;

    private int bossExists = 0;
    private Context context;
    private Activity activity;
    private Timer timer;
    private MusicThread musicThread;
    private BulletThread bulletThread;
    private FrozeThread frozeThread;
    private SuperThread superThread;

    /**
     * 难度有关参数
     */
    private int heroHp;
    private int maxHp;
    private int enemyMaxNumber;
    private int bossTarget;
    private int bossSpeedX;
    private int bossSPeedY;
    private int bossHp;
    private int mobSpeedX;
    private int mobSpeedY;
    private int mobHp;
    private int eliteSpeedX;
    private int eliteSpeedY;
    private int eliteHp;
    private int generateRate;
    private int timeRate;
    private int missileRate;
    private int missileSpeedY;
    private int bossShootRate;


    public Game(Context context) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        heroAircraft = HeroAircraft.getInstance(
                GameActivity.WINDOW_WIDTH / 2,
                GameActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, heroHp);

        eliteEnemyFactory = new EliteEnemyFactory();
        mobEnemyFactory = new MobEnemyFactory();
        bossEnemyFactory = new BossEnemyFactory();
        bloodPropFactory = new BloodPropFactory();
        bulletPropFactory = new BulletPropFactory();
        bombPropFactory = new BombPropFactory();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        contextShoot = new ContextShoot(new HeroStrategyNormal());
        musicThread = new MusicThread(context);

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        heroAircraft.refresh(GameActivity.WINDOW_WIDTH / 2,
                GameActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, maxHp);

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            musicThread.start(MusicThread.SOUND_BGM);
            time += timeInterval;
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if (bossScore >= bossTarget) {
                    if (bossExists == 0) {
                        musicThread.start(MusicThread.SOUND_BGM_BOSS);
                        musicThread.stop(MusicThread.SOUND_BGM);
                        bossExists = 1;
                        enemyAircrafts.add(bossEnemyFactory.createEnemy(GameActivity.WINDOW_WIDTH / 2,
                                50,
                                bossSpeedX + (int) (timeRate * time * Math.pow(10, -6)),
                                bossSPeedY,
                                bossHp + (int) (timeRate * time * Math.pow(10, -6))
                        ));
                    }
                }
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    if ((int) (Math.random() * 1000) % (generateRate + 1) != 0) {
                        enemyAircrafts.add(mobEnemyFactory.createEnemy(
                                (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                                (int) (Math.random() * GameActivity.WINDOW_HEIGHT * 0.2) * 1,
                                (mobSpeedX + (int) (timeRate * time * Math.pow(10, -6))) * (Math.random() > 0.5 ? 1 : -1),
                                mobSpeedY + (int) (timeRate * time * Math.pow(10, -6)),
                                mobHp + (int) (timeRate * time * Math.pow(10, -5))
                        ));
                    } else {
                        enemyAircrafts.add(eliteEnemyFactory.createEnemy(
                                (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())) * 1,
                                (int) (Math.random() * GameActivity.WINDOW_HEIGHT * 0.2) * 1,
                                (eliteSpeedX + (int) (timeRate * time * Math.pow(10, -6))) * (Math.random() > 0.5 ? 1 : -1),
                                eliteSpeedY + (int) (timeRate * time * Math.pow(10, -6)),
                                eliteHp + (int) (timeRate * time * Math.pow(10, -5))
                        ));
                    }
                }
                if ((int) (Math.random() * 1000) % (missileRate + 1) == 0) {
                    enemyAircrafts.add(new MissileEnemy(
                            (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                            0,
                            0,
                            missileSpeedY + (int) (timeRate * time * Math.pow(10, -6)),
                            Integer.MAX_VALUE
                    ));
                }
                // 飞机射出子弹
                shootAction();

            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            invalidate();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                musicThread.stop(MusicThread.SOUND_BGM);
                musicThread.stop(MusicThread.SOUND_BGM_BOSS);
                musicThread.start(MusicThread.SOUND_GAME_OVER);
                // 游戏结束
                Message message = new Message();
                message.what = GameActivity.GAME_OVER;
                message.arg1 = score;
                GameActivity.handler.sendMessage(message);

                timer.cancel();
                gameOverFlag = true;
                System.out.println("Game Over!");

            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(task).start();
            }
        }, timeInterval, timeInterval);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // TODO 敌机射击
        for (int i = 0; i < enemyAircrafts.size(); i++) {
            System.out.println(enemyAircrafts.get(i).getClass().getName());
            if (enemyAircrafts.get(i).getClass().getName().contains("BossEnemy")) {
                contextShoot.setStrategy(new BossStrategy());
                enemyBullets.addAll(contextShoot.executeStrategy(enemyAircrafts.get(i), 30, (int) (Math.random() * (bossShootRate + 4)), 1));
            } else if (enemyAircrafts.get(i).getClass().getName().contains("EliteEnemy")) {
                contextShoot.setStrategy(new EliteStrategy());
                enemyBullets.addAll(contextShoot.executeStrategy(enemyAircrafts.get(i), 30, 1, 1));
            }
        }
        // 英雄射击
        if (heroAircraft.getShootNum() == 1) {
            contextShoot.setStrategy(new HeroStrategyNormal());
            heroBullets.addAll(contextShoot.executeStrategy(heroAircraft, 30, 1, -1));
        } else {
            contextShoot.setStrategy(new HeroStrategySuper());
            heroBullets.addAll(contextShoot.executeStrategy(heroAircraft, 30, heroAircraft.getShootNum(), -1));
        }
    }

    private void bulletsMoveAction() {
        for (AbstractBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (AbstractBullet bullet : enemyBullets) {
            bullet.forward();
        }
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {

        for (AbstractBullet bullet : enemyBullets) {
            if (bullet.notValid())
                continue;
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (int i = 0; i < heroBullets.size(); i++) {
            AbstractBullet bullet = heroBullets.get(i);
            if (bullet.notValid()) {
                continue;
            }
            for (int j = 0; j < enemyAircrafts.size(); j++) {
                AbstractAircraft enemyAircraft = enemyAircrafts.get(j);
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
//                    musicThread.start(MusicThread.SOUND_BULLET_HIT);

                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if (enemyAircraft.getClass().getName().equals("edu.hitsz.aircraft.BossEnemy")) {
                            bossScore = 0;
                            bossExists = 0;
                            musicThread.stop(MusicThread.SOUND_BGM_BOSS);
                            musicThread.start(MusicThread.SOUND_BGM);
                        }

                        // TODO 获得分数，产生道具补给
                        else if (enemyAircraft.getClass().getName().equals("edu.hitsz.aircraft.EliteEnemy")) {
                            int random = (int) (Math.random() * 100);
                            if (random % 4 == 0)
                                props.add(bloodPropFactory.createProp(
                                        enemyAircraft.getLocationX(),
                                        enemyAircraft.getLocationY(),
                                        10,
                                        5,
                                        -30
                                ));
                            else if (random % 4 == 1) {
                                System.out.println("FireSupply active!");
                                props.add(bulletPropFactory.createProp(
                                        enemyAircraft.getLocationX(),
                                        enemyAircraft.getLocationY(),
                                        10,
                                        5,
                                        0
                                ));
                            } else if (random % 4 == 2) {
                                System.out.println("BombSupply active!");
                                props.add(bombPropFactory.createProp(
                                        enemyAircraft.getLocationX(),
                                        enemyAircraft.getLocationY(),
                                        10,
                                        5,
                                        -0
                                ));
                            }
                        }
                        score += 10;
                        bossScore += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    if(heroAircraft.getSuperState()==false){
                        enemyAircraft.vanish();
                        heroAircraft.decreaseHp(Integer.MAX_VALUE);
                    }
                    else {
                        enemyAircraft.vanish();
                        score+=10;
                    }

                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid())
                continue;
            if (heroAircraft.crash(prop)) {

                if (prop.getClass().getName().equals("edu.hitsz.prop.BulletProp")) {
                    if (bulletThread != null) {
                        bulletThread.interrupt();
                    }
                    bulletThread = new BulletThread(heroAircraft);
                    bulletThread.start();
                    musicThread.start(MusicThread.SOUND_GET_SUPPLY);
                    heroAircraft.setShootNum(heroAircraft.getShootNum() + 2);
                } else if (prop.getClass().getName().equals("edu.hitsz.prop.BloodProp")) {
                    musicThread.start(MusicThread.SOUND_GET_SUPPLY);
                    heroAircraft.decreaseHp(prop.getPower());
                } else if (prop.getClass().getName().equals("edu.hitsz.prop.BombProp")) {
                    musicThread.start(MusicThread.SOUND_BOMB_EXPLOSION);
                    for (AbstractAircraft abstractAircraft : enemyAircrafts) {
                        score += 10;
                        bossScore += 10;
                        ((BombProp) prop).addEnemy(abstractAircraft);
                    }
                    for (AbstractBullet abstractBullet : enemyBullets) {
                        ((BombProp) prop).addBullet(abstractBullet);
                    }
                    ((BombProp) prop).bomb();
                }
                prop.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g
     */

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制背景,图片滚动
        super.onDraw(canvas);
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - GameActivity.WINDOW_HEIGHT, null);
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == GameActivity.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(canvas, enemyBullets);
        paintImageWithPositionRevised(canvas, heroBullets);
        paintImageWithPositionRevised(canvas, props);
        paintImageWithPositionRevised(canvas, enemyAircrafts);

        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(canvas);
    }

    private void paintImageWithPositionRevised(Canvas g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : object.getClass().getName() + " has no image! ";
            g.drawBitmap(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Canvas g) {
        int x = GameActivity.WINDOW_WIDTH / 16;
        int y = GameActivity.WINDOW_HEIGHT / 16;

        Paint pen = new Paint();
        pen.setColor(Color.RED);
        pen.setTypeface(Typeface.SANS_SERIF);
        pen.setTextSize(GameActivity.WINDOW_WIDTH / 16);

        g.drawText("SCORE:" + this.score, x, y, pen);
        g.drawText("BOSS_SCORE:" + this.bossScore, x, y + GameActivity.WINDOW_HEIGHT / 32, pen);
        g.drawText("LIFE:" + this.heroAircraft.getHp(), x, y + GameActivity.WINDOW_HEIGHT / 16, pen);


    }


    public abstract void setDifficulty();

    public void setMissileSpeedY(int missileSpeedY) {
        this.missileSpeedY = missileSpeedY;
    }

    public void setMissileRate(int missileRate) {
        this.missileRate = missileRate;
    }

    public void setEnemyMaxNumber(int enemyMaxNumber) {
        this.enemyMaxNumber = enemyMaxNumber;
    }

    public void setBossTarget(int bossTarget) {
        this.bossTarget = bossTarget;
    }


    public void setBossSpeedX(int bossSpeedX) {
        this.bossSpeedX = bossSpeedX;
    }

    public void setBossShootRate(int bossShootRate) {
        this.bossShootRate = bossShootRate;
    }

    public void setBossSPeedY(int bossSPeedY) {
        this.bossSPeedY = bossSPeedY;
    }

    public void setBossHp(int bossHp) {
        this.bossHp = bossHp;
    }

    public void setMobSpeedX(int mobSpeedX) {
        this.mobSpeedX = mobSpeedX;
    }

    public void setMobSpeedY(int mobSpeedY) {
        this.mobSpeedY = mobSpeedY;
    }

    public void setMobHp(int mobHp) {
        this.mobHp = mobHp;
    }

    public void setEliteSpeedX(int eliteSpeedX) {
        this.eliteSpeedX = eliteSpeedX;
    }

    public void setEliteSpeedY(int eliteSpeedY) {
        this.eliteSpeedY = eliteSpeedY;
    }

    public void setEliteHp(int eliteHp) {
        this.eliteHp = eliteHp;
    }

    public void setGenerateRate(int generateRate) {
        this.generateRate = generateRate;
    }

    public void setTimeRate(int timeRate) {
        this.timeRate = timeRate;
    }

    public int getHeroHp() {
        return heroHp;
    }

    public void setHeroHp(int heroHp) {
        this.heroHp = heroHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void actionSuper(){
        superThread = new SuperThread(heroAircraft);
        superThread.start();
        heroAircraft.setSuperState(true);
    }

    public void actionFroze(){
        FrozeProp frozeProp = new FrozeProp(1,1,1,1,1);
        frozeThread= new FrozeThread(heroAircraft,frozeProp);
        frozeThread.start();
        for (AbstractAircraft abstractAircraft : enemyAircrafts) {
            ( frozeProp).addEnemy(abstractAircraft);
        }
        for (AbstractBullet abstractBullet : enemyBullets) {
            (frozeProp).addBullet(abstractBullet);
        }
        frozeProp.Froze();
        frozeProp.vanish();



    }

    public void actionBomb(){
        AbstractProp bombProp = new BombProp(1,1,1,1,1);
        musicThread.start(MusicThread.SOUND_BOMB_EXPLOSION);
        for (AbstractAircraft abstractAircraft : enemyAircrafts) {
            score += 10;
            bossScore += 10;
            ((BombProp) bombProp).addEnemy(abstractAircraft);
        }
        for (AbstractBullet abstractBullet : enemyBullets) {
            ((BombProp) bombProp).addBullet(abstractBullet);
        }
        ((BombProp) bombProp).bomb();
        bombProp.vanish();


    }

    public int getScore() {
        return score;
    }

    public boolean isGameOverFlag() {
        return gameOverFlag;
    }
}
