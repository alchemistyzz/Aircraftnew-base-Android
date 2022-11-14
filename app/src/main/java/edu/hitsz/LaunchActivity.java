//mysql_account:aircraft
//mysql_password:123Abc!333ka@
package edu.hitsz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.Contract;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
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
import edu.hitsz.data.Database;
import edu.hitsz.data.RankList;
import edu.hitsz.data.RankListAdapter;
import edu.hitsz.data.User;
import edu.hitsz.internet.Client;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;

public class LaunchActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LaunchActivity";

    public static Handler handler;

    private Button gameEasyBtn;
    private Button gameNormalBtn;
    private Button gameHardBtn;
    private Button combatBtn;
    private ImageButton shoppingBtn;
    private Button changeIDBtn;

    private Game game;
    private User user;
    private Intent intent;
    private Database database;
    private SimpleDateFormat simpleDateFormat;

    public static int USER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
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
        if (event.getAction() == MotionEvent.BUTTON_BACK) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {

    }

    private void loadingImg() {

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

    @SuppressLint({"HandlerLeak", "WrongViewCast"})
    private void initVar() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        database = Database.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        checkUser();
        user=database.getUserById(USER_ID);

        gameEasyBtn = findViewById(R.id.GameEasy);
        gameHardBtn = findViewById(R.id.GameHard);
        gameNormalBtn = findViewById(R.id.GameNormal);
        shoppingBtn = findViewById(R.id.shopping);
        combatBtn = findViewById(R.id.GameCombat);
        changeIDBtn=findViewById(R.id.changeID);

        gameEasyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                GameActivity.MUSIC_ON = checkBox.isChecked();
                intent = new Intent();
                intent.setClass(LaunchActivity.this, GameActivity.class);
                intent.putExtra("mode", "easy");
                startActivity(intent);
            }
        });
        gameNormalBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                GameActivity.MUSIC_ON = checkBox.isChecked();
                intent = new Intent();
                intent.setClass(LaunchActivity.this, GameActivity.class);
                intent.putExtra("mode", "normal");
                startActivity(intent);
            }
        });
        gameHardBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                GameActivity.MUSIC_ON = checkBox.isChecked();
                intent = new Intent();
                intent.setClass(LaunchActivity.this, GameActivity.class);
                intent.putExtra("mode", "hard");
                startActivity(intent);
            }
        });
        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = findViewById(R.id.SoundCheckBox);
                GameActivity.MUSIC_ON = checkBox.isChecked();
                intent = new Intent();
                intent.setClass(LaunchActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }


        });
        combatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CombatActivity.COMBAT_STATUS) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    intent = new Intent();
                    intent.setClass(LaunchActivity.this, CombatActivity.class);
                    startActivity(intent);
                }
            }
        });

        changeIDBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LaunchActivity.this);
                builder.setIcon(R.drawable.ic_launcher_background);
                builder.setTitle("请输入新的用户名");
                String oldUserName = user.getName();
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(LaunchActivity.this).inflate(R.layout.change_id_dialog, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
                TextView old_user_name = view.findViewById(R.id.oldUsername);
                EditText new_user_username = view.findViewById(R.id.newUsername);
                old_user_name.setText("旧用户名:"+oldUserName);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String newUserName = new_user_username.getText().toString().trim();
                        user.setName(newUserName);
                        database.updateUser(user);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<RankList> rankLists = new LinkedList<>();
                                rankLists.addAll(database.getAllRankListByUser(1,user.getId()));
                                rankLists.addAll(database.getAllRankListByUser(2,user.getId()));
                                rankLists.addAll(database.getAllRankListByUser(3,user.getId()));
                                for(int i=0;i<rankLists.size();i++){
                                    RankList rankList=rankLists.get(i);
                                    rankList.setUser_name(user.getName());
                                    database.updateRanklist(rankList);
                                }
                            }
                        }).start();

                        //    将输入的新用户名打印出来
                        Toast.makeText(LaunchActivity.this, "新用户名: " + newUserName , Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });


        DisplayMetrics dm = getResources().getDisplayMetrics();
        GameActivity.WINDOW_HEIGHT = dm.heightPixels;
        GameActivity.WINDOW_WIDTH = dm.widthPixels;


    }


    private void checkUser() {
        if (!database.userExistByAccount(LoginActivity.OPEN_ID)) {
            database.addUser(new User("用户" + LoginActivity.OPEN_ID.substring(0, 12), LoginActivity.OPEN_ID, "12345678", simpleDateFormat.format(new Date()), 0, "", 5, 5, 5));
        }
        USER_ID = database.getUserByAccount(LoginActivity.OPEN_ID).getId();
    }


}