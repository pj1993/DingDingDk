package com.netease.uu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * create by pj on 2019/5/6
 * 刺.刺.刺激...
 */
public class Utils {

    public static int[] getCalendarHm(Calendar cal){
        int hours = cal.get( Calendar.HOUR_OF_DAY );
        int minute = cal.get( Calendar.MINUTE );
//        int second = cal.get( Calendar.SECOND );
        return new int[]{hours,minute};
    }

    public static int getRandom(int start,int end){
        Random rand = new Random();
        if(start>=end){
            return start;
        }
        return rand.nextInt(end - start) + start;
    }



    public static String getTime(Date date) {
        try {
            SimpleDateFormat formatter=new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            String format = formatter.format(date);
            return format;
        } catch (Exception e) {
            return "";
        }
    }
}
