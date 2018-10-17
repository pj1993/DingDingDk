package com.netease.uu;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.netease.uu.MainActivity.SPNAME;

/**
 * Created by pj on 2018/7/6.
 * 刺..刺.刺激1
 * 打卡的操作
 */
public class DKservice extends IntentService {
    private OutputStream os;
    private static final String DD_PACKAGENAME="com.alibaba.android.rimet";
    private static final String DD_ACTIVITY_DK="com.alibaba.lightapp.runtime.activity.CommonWebViewActivity";
    public static final String sreanBitmap=Environment.getExternalStorageDirectory()+"/DDZDDK_screan.png";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  name Used to name the worker thread, important only for debugging.
     */
    public DKservice() {
        super("DKservice");
    }

    @Override
    public void onCreate() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        wakeUpAndUnlock(getApplicationContext());//解锁
        //滑动屏幕防止解锁失败
//        huadong("300","1000","360","500");
        SystemClock.sleep(5000);
        //打开钉钉
        startDingDingDkActivity();
        SystemClock.sleep(5000);

//        startDkActivity();

//        //点击中间菜单
        pointXY("360","1230");//0.5,  0.97
        SystemClock.sleep(5000);
//        //将打卡功能上划出来
        huadong("360","1100","160","600");
        SystemClock.sleep(2000);
        //1截屏  2.等分24份  3文字识别《考勤打卡》  4计算位置  5，执行下面代码
//        pointXY("102","984");//1/8,  y-(y*0.2529+50)
        //点击考勤打卡功能按钮
        SharedPreferences sp = getSharedPreferences(SPNAME, MODE_PRIVATE);
        String x=sp.getString("clickX","102");
        String y=sp.getString("clickY","844");
        pointXY(x,y);//1/8,  y-(y*0.2529+50)

        SystemClock.sleep(10000);

        //打上班卡
        pointXY("360","550");
        SystemClock.sleep(2000);
        //打下班卡
        pointXY("360","724");//0.5,  y-(y*0.2529+50)
        //退出钉钉
        SystemClock.sleep(10000);
        stopDingDingDkActivity(DD_PACKAGENAME);
        //关闭通道
        try {
            if(os!=null){
                os.close();
            }
            os=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //本方法完后会自动调用stopSelf();
        onDestroy();//手动销毁service
    }

    /**
     * 图片识别
     */
    public void identifyImg(){
        Bitmap bitmap= BitmapFactory.decodeFile(sreanBitmap);
        //灰度处理
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        for(int i=0;i<6;i++){
            for(int j=0;j<4;j++){
                Bitmap img = Bitmap.createBitmap(bitmap, width / 4 * j, height / 6 * i, width / 4, height / 6);
            }
        }
    }


    /**
     * 打开钉钉打卡
     */
    public void startDingDingDkActivity(){
        Intent intent = this.getPackageManager().getLaunchIntentForPackage(DD_PACKAGENAME);
//        Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.pj.dingdingdk");
        startActivity(intent);
    }



    /**
     * 解锁
     * @param context
     */
    public static void wakeUpAndUnlock(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }


    /**---------------------------------------------------下面是adb模拟屏幕事件，点击---------------------------------------------------------------------------------*/

    public void stopDingDingDkActivity(String packageName){
        String cmd = "am force-stop " + packageName + " \n";
        exec(cmd);
    }
    /**
     * 跳转到打卡界面(他并不就是一个activity，无法再钉钉上使用)
     */
    public void startDkActivity(){
        String cmd=String.format("am start -n %s/%s \n",DD_PACKAGENAME,DD_ACTIVITY_DK);
        exec(cmd);
    }
    /**
     * 滑动屏幕
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void huadong(String x1,String y1,String x2,String y2){
        String cmd=String.format("input swipe %s %s %s %s \n",x1,y1,x2,y2);
        exec(cmd);
    }
    /**
     * 点击
     * @param x
     * @param y
     */
    public void pointXY(String x,String y){
        String cmd=String.format("input tap %s %s \n",x,y);
        exec(cmd);
    }
    /**
     * 截屏
     */
    public void capScrean(){
        exec("screencap -p "+sreanBitmap+" \n");
    }
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
//            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GK", e.getMessage());
        }
    }


    /**
     * 如果前台APP是目标apk
     */
    private boolean isCurrentAppIsTarget() {
        String name = getForegroundAppPackageName();
        if (name!=null&&!"".equals(name) && "".equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }
    /**
     * 获取前台程序包名，该方法仅在android L之前有效
     */
    public String getForegroundAppPackageName() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lr = am.getRunningAppProcesses();
        if (lr == null) {
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo ra : lr) {
            if (ra.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE || ra.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.i("GK", ra.processName);
                return ra.processName;
            }
        }
        return "";
    }
}