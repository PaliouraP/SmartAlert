package com.unipi.p19129_p19140.smartalert;

import java.util.ArrayList;

public class ReportModel {

    String Type, Location, Timestamp, status, User;
    int reporter_sum;
    ArrayList<String> reports;

    public String getType() {
        return Type;
    }

    public String getLocation() {
        return Location;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getUser() {
        return User;
    }

    public int getReporter_sum() {
        return reporter_sum;
    }
}
