package com.netease.uu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private EditText et_time;
    public static final String SPNAME="time_later";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startService(new Intent(this,PushService.class));
        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this,PushService.class));
                exec("input tap 500 500 \n");//获取root权限
            }
        });
        et_time = (EditText) findViewById(R.id.et_time);
        sp = getSharedPreferences(SPNAME, MODE_PRIVATE);
        int time = sp.getInt("time", 15);
        et_time.setText(time+"");
        et_time.addTextChangedListener(new MyTextWatcher(R.id.et_time));
    }


    private OutputStream os;
    /**
     * 执行ADB命令： input tap 125 340
     */
    public final void exec(String cmd) {
        Log.e("eee",cmd);
        try {
            if (os == null) {
                os = Runtime.getRuntime().exec("su").getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GK", e.getMessage());
        }
    }


    public class MyTextWatcher implements TextWatcher{
        private int res;
        public MyTextWatcher(int res) {
            this.res = res;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            switch (res) {
                case R.id.et_time:
                    SharedPreferences.Editor edit = sp.edit();
                    int time = Integer.parseInt(s.toString().trim());
                    edit.putInt("time",time);
                    edit.commit();
                    break;
            }
        }
    }
}
