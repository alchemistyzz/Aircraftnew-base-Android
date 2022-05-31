package edu.hitsz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MissileEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.Game;
import edu.hitsz.application.GameEasy;
import edu.hitsz.application.GameHard;
import edu.hitsz.application.GameNormal;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.data.RankList;
import edu.hitsz.data.RankListAdapter;
import edu.hitsz.data.RankListDaoImpl;
import edu.hitsz.internet.Server;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;

public class LaunchActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LaunchActivity";

    public static int WINDOW_HEIGHT;
    public static int WINDOW_WIDTH;

    public static int GAME_OVER = 1;
    public static int NAME_INPUT = 2;
    public static int GAME_MODE;
    public static boolean MUSIC_ON = false;

    public static Handler handler;

    private Button gameEasyBtn;
    private Button gameNormalBtn;
    private Button gameHardBtn;

    private ListView listView;

    private Game game;
    private RankListDaoImpl rankListDao;
    private List<RankList> rankLists;
    private Server server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                se.sendMessage();
//            }
//        }).start();


        initVar();
        loadingImg();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(LaunchActivity.this, "MainHelloService onDestroy",
                Toast.LENGTH_SHORT).show();
        Log.i(TAG, "MainHelloService onDestroy");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指按下的时候
            game.heroAircraft.setLocation(event.getX(), event.getY());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {

    }

    public void loadingImg() {

        ImageManager.MISSILE_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.missile_enemy);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.BOSS_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.BLOOD_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.BOMB_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.BULLET_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);

        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), ImageManager.HERO_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), ImageManager.BOSS_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), ImageManager.MOB_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ImageManager.ELITE_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), ImageManager.HERO_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ImageManager.ENEMY_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), ImageManager.BLOOD_PROP_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), ImageManager.BOMB_PROP_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), ImageManager.BULLET_PROP_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(MissileEnemy.class.getName(), ImageManager.MISSILE_IMAGE);
    }

    @SuppressLint("HandlerLeak")
    public void initVar() {
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GAME_OVER) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                    builder.setTitle("请输入姓名");
                    EditText nameView = new EditText(LaunchActivity.this);
                    nameView.setText("testUser");
                    int score = msg.arg1;
                    builder.setView(nameView);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = nameView.getText().toString();
                            setContentView(R.layout.ranklist);
                            TextView text = findViewById(R.id.difficultTextView);
                            listView = findViewById(R.id.rankListView);
                            if (GAME_MODE == 1) {
                                text.setText(R.string.gameEasyText);
                            } else if (GAME_MODE == 2) {
                                text.setText(R.string.gameNormalText);
                            } else {
                                text.setText(R.string.gameHardText);
                            }

                            rankListDao = new RankListDaoImpl();
                            Date day = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            RankList tem = new RankList(score + "", name, sdf.format(day) + "", LaunchActivity.GAME_MODE + "");
                            rankListDao.doAdd(tem);
                            rankListDao.writeAll();
                            rankLists = rankListDao.queryAll();
                            RankListAdapter rankListAdapter = new RankListAdapter(rankLists, LaunchActivity.this);
                            listView.setAdapter(rankListAdapter);
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    rankListDao.deleteList(rankLists.get(position));
                                    rankListDao.writeAll();
                                    rankListDao = new RankListDaoImpl();
                                    rankLists = rankListDao.queryAll();
                                    RankListAdapter rankListAdapter = new RankListAdapter(rankLists, LaunchActivity.this);
                                    listView.setAdapter(rankListAdapter);
                                    return false;
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setContentView(R.layout.ranklist);
                            TextView text = findViewById(R.id.difficultTextView);
                            if (GAME_MODE == 1) {
                                text.setText(R.string.gameEasyText);
                            } else if (GAME_MODE == 2) {
                                text.setText(R.string.gameNormalText);
                            } else {
                                text.setText(R.string.gameHardText);
                            }

                            rankListDao = new RankListDaoImpl();
                            rankLists = rankListDao.queryAll();
                            RankListAdapter rankListAdapter = new RankListAdapter(rankLists, LaunchActivity.this);
                            listView.setAdapter(rankListAdapter);

                        }
                    });
                    builder.show();

                }

            }

        };


        gameEasyBtn = findViewById(R.id.GameEasy);
        gameHardBtn = findViewById(R.id.GameHard);
        gameNormalBtn = findViewById(R.id.GameNormal);
        listView = findViewById(R.id.rankListView);

        gameEasyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GAME_MODE = 1;
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                MUSIC_ON = checkBox.isChecked();
                ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
                game = new GameEasy(LaunchActivity.this);
                game.setDifficulty();
                game.action();
                setContentView(game);
            }
        });
        gameNormalBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GAME_MODE = 2;
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                MUSIC_ON = checkBox.isChecked();
                ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
                game = new GameNormal(LaunchActivity.this);
                game.setDifficulty();
                game.action();
                setContentView(game);
            }
        });
        gameHardBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GAME_MODE = 3;
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                MUSIC_ON = checkBox.isChecked();
                ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
                game = new GameHard(LaunchActivity.this);
                game.setDifficulty();
                game.action();
                setContentView(game);
            }
        });

        DisplayMetrics dm = getResources().getDisplayMetrics();
        WINDOW_HEIGHT = dm.heightPixels;
        WINDOW_WIDTH = dm.widthPixels;

        server = new Server("106.52.3.181");
        server.sendMessage("connect!!!");
    }


}