package com.unipi.p19129_p19140.smartalert;

import android.util.Log;

import java.lang.Math.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DistanceBetween {
    public SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
    public boolean checkDistance(String loc_1, String loc_2)
    {
        String[] location_1 = loc_1.split(",");
        String[] location_2 = loc_2.split(",");

        double dis = Math.sqrt(Math.pow((Double.parseDouble(location_1[0])-Double.parseDouble(location_2[0])),2) + Math.pow((Double.parseDouble(location_1[1])-Double.parseDouble(location_2[1])),2));

        return dis < 4;
    }

    public boolean checkDuration(String sdate1, String sdate2)
    {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            cal1.setTime(format.parse(sdate1));
            cal2.setTime(format.parse(sdate2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long target = cal1.getTimeInMillis();
        long original = cal2.getTimeInMillis();


        return TimeUnit.MILLISECONDS.toHours(Math.abs(target - original)) < 2;
    }

    public boolean beforeNow(String date){
        Date target_date = new Date();

        Date now = new Date();
        Log.d("now1", String.valueOf(now));
        try {
            target_date = format.parse(date);

            now = format.parse(String.valueOf(now));
            Log.d("now2", String.valueOf(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return target_date.before(now);

    }
}
