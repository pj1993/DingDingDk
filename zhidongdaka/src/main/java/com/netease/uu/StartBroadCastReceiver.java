package com.netease.uu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by pj on 2018/8/6.
 * 刺.刺.刺激
 * 开机自启监听
 */
public class StartBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //启动常驻服务，监听闹钟
        context.startService(new Intent(context,PushService.class));
    }
}
