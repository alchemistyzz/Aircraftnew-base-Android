package edu.hitsz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hitsz.application.Game;
import edu.hitsz.application.GameEasy;
import edu.hitsz.application.GameHard;
import edu.hitsz.application.GameNormal;
import edu.hitsz.application.ImageManager;
import edu.hitsz.data.Database;
import edu.hitsz.data.RankList;
import edu.hitsz.data.RankListAdapter;
import edu.hitsz.data.User;

public class GameActivity extends Activity {

    public static int GAME_OVER = 1;
    public static int SYNC_SCORE = 2;
    public static int GAME_MODE;
    public static boolean MUSIC_ON = false;
    public static int WINDOW_HEIGHT;
    public static int WINDOW_WIDTH;

    public int score;

    public static Handler handler;

    private Intent intent;
    public Game game;
    private ListView listView;
    private Database database;
    private User user;

    private SimpleDateFormat simpleDateFormat;
    private List<RankList> rankLists;

    private ImageButton superPropBtn;
    private ImageButton frozePropBtn;
    private ImageButton bombPropBtn;
    private Button GlobalBtn;
    private Button UserBtn;

    private TextView superText;
    private TextView frozeText;
    private TextView bombText;
    private TextView syncScoreText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        addBtnListener();
        addLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(game.heroAircraft.getLocationX() - event.getX()) < 200 && Math.abs(game.heroAircraft.getLocationY() - event.getY() + 200) < 200)
                game.heroAircraft.setLocation(event.getX(), event.getY() - 200);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (game.heroAircraft.notValid()) {
                System.out.println("Destroy!");
                intent = new Intent(GameActivity.this, LaunchActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                System.out.println("好好打游戏");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @SuppressLint("HandlerLeak")
    public void initVar() {
        intent = getIntent();
        database = Database.getInstance();
        user = database.getUserById(LaunchActivity.USER_ID);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        superPropBtn = new ImageButton(this);
        superPropBtn.setImageResource(R.drawable.prop_lift);
        bombPropBtn = new ImageButton(this);
        bombPropBtn.setImageResource(R.drawable.prop_bomb);
        frozePropBtn = new ImageButton(this);
        frozePropBtn.setImageResource(R.drawable.prop_froze);
        bombText = new TextView(this);
        bombText.setText(user.getBomb_prop_cnt() + "");
        superText = new TextView(this);
        superText.setText(user.getSuper_prop_cnt() + "");
        frozeText = new TextView(this);
        frozeText.setText(user.getFroze_prop_cnt() + "");
        syncScoreText = new TextView(this);


        if (intent.getStringExtra("mode").equals("easy")) {
            GAME_MODE = 1;
            ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
            game = new GameEasy(this);
            game.setDifficulty();
            game.action();
            setContentView(game);
        } else if (intent.getStringExtra("mode").equals("normal")) {
            GAME_MODE = 2;
            ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
            game = new GameNormal(this);
            game.setDifficulty();
            game.action();
            setContentView(game);
        } else if (intent.getStringExtra("mode").equals("hard")) {
            GAME_MODE = 3;
            ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
            game = new GameHard(this);
            game.setDifficulty();
            game.action();
            setContentView(game);
        } else if (intent.getStringExtra("mode").equals("combat")) {
            GAME_MODE = 2;
            ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
            game = new GameNormal(this);
            Message msg = new Message();
            msg.what = CombatActivity.SYNC_SCORE;
            msg.obj = game;
            CombatActivity.handler.sendMessage(msg);
            game.setDifficulty();
            game.action();
            setContentView(game);
            LinearLayout.LayoutParams scoreLayoutParams = new LinearLayout.LayoutParams(WINDOW_WIDTH / 2, WINDOW_WIDTH / 2);
            scoreLayoutParams.setMargins(WINDOW_WIDTH / 12, WINDOW_HEIGHT * 3 / 12, 0, 0);

            syncScoreText.setText("敌机分数为：0");
            syncScoreText.setTextColor(Color.RED);
            syncScoreText.setTextSize(22);
            addContentView(syncScoreText, scoreLayoutParams);
        }

        handler = new Handler() {
            @SuppressLint({"SetTextI18n", "HandlerLeak"})
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GAME_OVER) {
                    score = msg.arg1;
                    user.setMoney(user.getMoney()+score);
                    database.updateUser(user);
                    setContentView(R.layout.ranklist);
                    TextView text = findViewById(R.id.difficultTextView);
                    RankList rankList = new RankList(score, user.getName(), user.getId(), simpleDateFormat.format(new Date()), GAME_MODE);
                    database.addRankList(rankList);
                    rankLists = database.getAllRankListByUser(GAME_MODE, user.getId());
                    RankListAdapter rankListAdapter = new RankListAdapter(rankLists, GameActivity.this);
                    listView = findViewById(R.id.rankListView);
                    listView.setAdapter(rankListAdapter);
                    if (GAME_MODE == 1) {
                        text.setText("简单模式(个人排行榜)");
                    } else if (GAME_MODE == 2) {
                        text.setText("普通模式(个人排行榜)");
                    } else {
                        text.setText("困难模式(个人排行榜)");
                    }
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            database.deleteRankList(rankLists.get(position));
                            rankLists = database.getAllRankListByUser(GAME_MODE, user.getId());
                            RankListAdapter rankListAdapter = new RankListAdapter(rankLists, GameActivity.this);
                            listView.setAdapter(rankListAdapter);
                            return false;
                        }
                    });

                    GlobalBtn = findViewById(R.id.GlobalBtn);
                    GlobalBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rankLists = database.getAllRankListGlobal(GAME_MODE);
                            RankListAdapter rankListAdapter = new RankListAdapter(rankLists, GameActivity.this);
                            listView = findViewById(R.id.rankListView);
                            listView.setAdapter(rankListAdapter);
                            if (GAME_MODE == 1) {
                                text.setText("简单模式(全球排行榜)");
                            } else if (GAME_MODE == 2) {
                                text.setText("普通模式(全球排行榜)");
                            } else {
                                text.setText("困难模式(全球排行榜)");
                            }
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    return true;
                                }
                            });

                        }
                    });
                    UserBtn = findViewById(R.id.UserBtn);
                    UserBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rankLists = database.getAllRankListByUser(GAME_MODE, user.getId());
                            RankListAdapter rankListAdapter = new RankListAdapter(rankLists, GameActivity.this);
                            listView = findViewById(R.id.rankListView);
                            listView.setAdapter(rankListAdapter);
                            if (GAME_MODE == 1) {
                                text.setText("简单模式(个人排行榜)");
                            } else if (GAME_MODE == 2) {
                                text.setText("普通模式(个人排行榜)");
                            } else {
                                text.setText("困难模式(个人排行榜)");
                            }
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    database.deleteRankList(rankLists.get(position));
                                    rankLists = database.getAllRankListByUser(GAME_MODE, user.getId());
                                    RankListAdapter rankListAdapter = new RankListAdapter(rankLists, GameActivity.this);
                                    listView.setAdapter(rankListAdapter);
                                    return false;
                                }
                            });
                        }
                    });


                } else if (msg.what == SYNC_SCORE) {
                    syncScoreText.setText("敌机分数为："+(String) msg.obj);
                }
            }

        };


    }


    public void addLayout() {
        LinearLayout.LayoutParams propLayoutParams = new LinearLayout.LayoutParams(WINDOW_WIDTH / 8, WINDOW_WIDTH / 8);
        propLayoutParams.setMargins(WINDOW_WIDTH / 10, WINDOW_HEIGHT * 8 / 12, 0, 0);
        addContentView(superPropBtn, propLayoutParams);
        propLayoutParams.setMargins(WINDOW_WIDTH / 10, WINDOW_HEIGHT * 9 / 12, 0, 0);
        addContentView(frozePropBtn, propLayoutParams);
        propLayoutParams.setMargins(WINDOW_WIDTH / 10, WINDOW_HEIGHT * 10 / 12, 0, 0);
        addContentView(bombPropBtn, propLayoutParams);

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(WINDOW_WIDTH / 8, WINDOW_WIDTH / 8);
        textLayoutParams.setMargins(WINDOW_WIDTH / 10, WINDOW_HEIGHT * 78 / 120, 0, 0);
        addContentView(superText, textLayoutParams);
        textLayoutParams.setMargins(WINDOW_WIDTH / 10, WINDOW_HEIGHT * 88 / 120, 0, 0);
        addContentView(frozeText, textLayoutParams);
        textLayoutParams.setMargins(WINDOW_WIDTH / 10, WINDOW_HEIGHT * 98 / 120, 0, 0);
        addContentView(bombText, textLayoutParams);
    }


    public void addBtnListener() {
        superPropBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (user.getSuper_prop_cnt() > 0) {
                    user.setSuper_prop_cnt(user.getSuper_prop_cnt() - 1);
                    superText.setText(user.getSuper_prop_cnt() + "");
                    database.updateUser(user);
                    game.actionSuper();
                } else {

                }
            }
        });
        frozePropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getFroze_prop_cnt() > 0) {
                    user.setFroze_prop_cnt(user.getFroze_prop_cnt() - 1);
                    frozeText.setText(user.getFroze_prop_cnt() + "");
                    database.updateUser(user);
                    game.actionFroze();
                } else {

                }
            }
        });
        bombPropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getBomb_prop_cnt() > 0) {
                    user.setBomb_prop_cnt(user.getBomb_prop_cnt() - 1);
                    bombText.setText(user.getBomb_prop_cnt() + "");
                    database.updateUser(user);
                    game.actionBomb();
                } else {

                }
            }
        });


    }


}