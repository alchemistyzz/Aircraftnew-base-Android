package edu.hitsz.thread;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.rtp.AudioStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.hitsz.GameActivity;
import edu.hitsz.LaunchActivity;
import edu.hitsz.R;


/**
 * @author night
 */
public class MusicThread {
    public static int SOUND_BGM = 1;
    public static int SOUND_BGM_BOSS = 2;
    public static int SOUND_BULLET = 3;
    public static int SOUND_BULLET_HIT = 4;
    public static int SOUND_BOMB_EXPLOSION = 5;
    public static int SOUND_GAME_OVER = 6;
    public static int SOUND_GET_SUPPLY = 7;
    public static boolean isOn = false;
    private boolean BGM_ON = false;
    private boolean BGM_BOSS_ON = false;
    private Context context;
    private static SoundPool soundPool = new SoundPool(64, AudioManager.STREAM_MUSIC, 0);
    private static MediaPlayer mediaBgm;
    private static MediaPlayer mediaBgmBoss;
    private static HashMap<Integer, Integer> hashMap = new HashMap<>();

    public MusicThread(Context context) {
        this.context = context;
        isOn= GameActivity.MUSIC_ON;

        hashMap.put(SOUND_BGM, soundPool.load(context, R.raw.bgm, 1));
        hashMap.put(SOUND_BGM_BOSS, soundPool.load(context, R.raw.bgm_boss, 1));
        hashMap.put(SOUND_BULLET, soundPool.load(context, R.raw.bullet, 1));
        hashMap.put(SOUND_BOMB_EXPLOSION, soundPool.load(context, R.raw.bomb_explosion, 1));
        hashMap.put(SOUND_BULLET_HIT, soundPool.load(context, R.raw.bullet_hit, 1));
        hashMap.put(SOUND_GAME_OVER, soundPool.load(context, R.raw.game_over, 1));
        hashMap.put(SOUND_GET_SUPPLY, soundPool.load(context, R.raw.get_supply, 1));
    }

    public void start(int id) {
        if (isOn) {
            if (id == SOUND_BGM) {
                if (BGM_ON == false) {
                    BGM_ON = true;
                    mediaBgm = MediaPlayer.create(context, R.raw.bgm);
                    mediaBgm.setLooping(true);
                    mediaBgm.start();
                }
            } else if (id == SOUND_BGM_BOSS) {
                if (BGM_BOSS_ON == false) {
                    BGM_BOSS_ON = true;
                    mediaBgmBoss = MediaPlayer.create(context, R.raw.bgm_boss);
                    mediaBgmBoss.setLooping(true);
                    mediaBgmBoss.start();
                }
            } else {
                soundPool.play(hashMap.get(id), 1, 1, 0, 0, 1);
            }
        }
    }


    public void stop(int id) {
        if (id == SOUND_BGM_BOSS) {
            if (BGM_BOSS_ON) {
                BGM_BOSS_ON = false;
                mediaBgmBoss.stop();
                mediaBgmBoss.release();
            }
        } else if (id == SOUND_BGM) {
            if (BGM_ON) {
                BGM_ON = false;
                mediaBgm.stop();
                mediaBgm.release();
            }
        } else {
            soundPool.stop(hashMap.get(id));
        }
    }


}


