package com.netease.uu;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by pj on 2018/9/10.
 * 刺.刺.刺激
 * 无障碍服务
 */
public class ActivityWatchService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            AccessibilityNodeInfo node = getRootInActiveWindow();
        }
    }

    @Override
    public void onInterrupt() {

    }
}
