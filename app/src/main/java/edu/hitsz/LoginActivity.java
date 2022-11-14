package edu.hitsz;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.open.log.SLog;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import edu.hitsz.data.Database;
import edu.hitsz.data.User;


public class LoginActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private static String APP_ID = "1105546048";
    public static Tencent tencent;
    public static String OPEN_ID;
    public static Handler handler;

    private Intent intent;
    private IUiListener loginListener;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initVar();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void initVar() {
        try {
            Tencent.setIsPermissionGranted(true);
            tencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
            initListener();
            initBtn();
            initHandler();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void initBtn() {
        loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tencent.logout(LoginActivity.this);
                tencent.login(LoginActivity.this, "all", loginListener, true);
            }
        });
    }

    public void initListener() {
        loginListener = new BaseUiListener() {
            @Override
            protected void doComplete(JSONObject values) {
                initOpenidAndToken(values);
                OPEN_ID = tencent.getOpenId();
                intent = new Intent();
                intent.setClass(LoginActivity.this, LoadingActivity.class);
                startActivity(intent);
            }
        };

    }

    @SuppressLint("HandlerLeak")
    public void initHandler() {
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWarning(int i) {

        }
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                tencent.setAccessToken(token, expires);
                tencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }


}