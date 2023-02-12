package com.unipi.p19129_p19140.smartalert;

import java.lang.Math.*;

public class DistanceBetween {
    public boolean checkDistance(String loc_1, String loc_2)
    {
        String[] location_1 = loc_1.split(",");
        String[] location_2 = loc_2.split(",");

        double dis = Math.sqrt(Math.pow((Double.parseDouble(location_1[0])-Double.parseDouble(location_2[0])),2) + Math.pow((Double.parseDouble(location_1[1])-Double.parseDouble(location_2[1])),2));

        return dis < 4;
    }
}
