package com.netease.uu;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by pj on 2018/7/20.
 * 刺..刺.刺激
 */

public class PushService extends Service {
    public PushService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.android.deskclock.ALARM_ALERT");
        filter.addAction("com.android.alarmclock.ALARM_ALERT");
        filter.addAction("com.lge.clock.alarmclock.ALARM_ALERT");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT");
        filter.addAction("com.sonyericsson.alarm.ALARM_ALERT");
        filter.addAction("com.htc.android.worldclock.ALARM_ALERT");
        filter.addAction("com.htc.worldclock.ALARM_ALERT");
        filter.addAction("com.lenovomobile.deskclock.ALARM_ALERT");
        filter.addAction("com.cn.google.AlertClock.ALARM_ALERT");
        filter.addAction("com.htc.android.worldclock.intent.action.ALARM_ALERT");
        filter.addAction("com.lenovo.deskclock.ALARM_ALERT");
        filter.addAction("com.oppo.alarmclock.alarmclock.ALARM_ALERT");
        filter.addAction("com.zdworks.android.zdclock.ACTION_ALARM_ALERT");
        registerReceiver(new DKReceiver(),filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class DKReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context,DKservice.class));
        }
    }


}
