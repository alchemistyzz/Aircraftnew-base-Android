package edu.hitsz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.logging.LogRecord;
import java.util.logging.SocketHandler;

import edu.hitsz.application.Game;
import edu.hitsz.application.GameNormal;
import edu.hitsz.application.ImageManager;
import edu.hitsz.data.Database;
import edu.hitsz.internet.Client;


public class CombatActivity extends AppCompatActivity {

    public static boolean COMBAT_STATUS = false;

    public static int START_COMBAT = 1;
    public static int SYNC_SCORE = 2;

    public static Handler handler;
    private Intent intent;
    private Database database;
    private Client client;
    private Game game;
    private Context context;

    private Button combatQuitBtn;
    private Thread waitThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        initVar();
        initHandler();
        waitCombat();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initVar() {
        client = Client.getInstance();
        combatQuitBtn = findViewById(R.id.combat_quit_button);

        combatQuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendMessage("quitCombat");
                waitThread.interrupt();
                intent = new Intent();
                intent.setClass(CombatActivity.this, LaunchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void waitCombat() {
        try {
            Thread.sleep((int) (Math.random() * 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.sendMessage("waitForCombat");
        waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    String receiveMsg;
                    try {
                        if (client.getInputReader().ready()) {
                            if ((receiveMsg = client.getMessage()) != null) {
                                if (receiveMsg.equals("startCombat")) {
                                    Message msg = new Message();
                                    msg.what = CombatActivity.START_COMBAT;
                                    CombatActivity.handler.sendMessage(msg);
                                    return;
                                }
                            }
                        }
                        Thread.sleep(200);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        waitThread.start();
    }


    private void startCombat() {
        intent = new Intent();
        intent.setClass(CombatActivity.this, GameActivity.class);
        intent.putExtra("mode", "combat");
        startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == START_COMBAT) {
                    startCombat();
                } else if (msg.what == SYNC_SCORE) {
                    game = (Game) msg.obj;
                    new Thread(new SyncScoreThread(game)).start();
                }
            }
        };
    }

    private class SyncScoreThread extends Thread {
        private Game game;

        SyncScoreThread(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            try {
                COMBAT_STATUS = true;
                while (true) {
                    System.out.println("score:" + this.game.getScore());
                    if (this.game.isGameOverFlag())
                        client.sendMessage("GameOver");
                    else {
                        System.out.println("sendMessage1");
                        client.sendMessage(this.game.getScore() + "");
                        System.out.println("sendMessage2");
                    }
                    String score = client.getMessage();
                    Message msg = new Message();
                    msg.what = GameActivity.SYNC_SCORE;
                    msg.obj = score;
                    GameActivity.handler.sendMessage(msg);
                    if (score.equals("combatOver"))
                        break;
                    System.out.println("getScore" + score);
                    Thread.sleep(200);
                }
                COMBAT_STATUS = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}