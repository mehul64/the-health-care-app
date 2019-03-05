package com.thc.app.Interfaces;

import com.jjoe64.graphview.series.DataPointInterface;

import java.util.ArrayList;

/**
 * Created by HP on 17-06-2017.
 */

public class MyDataPoint implements DataPointInterface {

    ArrayList<String> statusList;
    int x;
    String y;

    public MyDataPoint(int x, String y) {
        this.x = x;
        this.y = y;

        statusList=new ArrayList<>();
        statusList.add("bad");
        statusList.add("average");
        statusList.add("good");
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return statusList.indexOf(y);
    }
}
