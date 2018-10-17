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
    private EditText et_x;
    private EditText et_y;
    public static final String SPNAME="location";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,PushService.class));
        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this,PushService.class));
                exec("input tap 500 500 \n");//获取root权限
            }
        });
        et_x = (EditText) findViewById(R.id.et_x);
        et_y = (EditText) findViewById(R.id.et_y);
        sp = getSharedPreferences(SPNAME, MODE_PRIVATE);
        String clickX = sp.getString("clickX", "102");
        String clickY= sp.getString("clickY","844");
        et_x.setText(clickX);
        et_y.setText(clickY);
        et_x.addTextChangedListener(new MyTextWatcher(R.id.et_x));
        et_y.addTextChangedListener(new MyTextWatcher(R.id.et_y));
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(this, "您当前点击的是:x"+event.getX()+",y"+event.getY(), Toast.LENGTH_SHORT).show();
        return super.onTouchEvent(event);
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
            String key="";
            switch (res) {
                case R.id.et_x:
                    key="clickX";
                    break;
                case R.id.et_y:
                    key="clickY";
                    break;
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(key,s.toString().trim());
            edit.commit();
        }
    }
}
