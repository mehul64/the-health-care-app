package com.thc.app.models;

/**
 * Created by aabid-personal on 9/25/16.
 */

public class OneDayData {

    String breakFast="";
    long breakFastCal;

    String lunch="";
    long lunchCal;

    String dinner="";
    long dinnerCal;

    String dateString;

    float walking;
    float running;
    float other;

    String result;

    public OneDayData(){

    }

    public String getBreakFast() {
        return breakFast;
    }

    public void setBreakFast(String breakFast) {
        this.breakFast = breakFast;
    }

    public long getBreakFastCal() {
        return breakFastCal;
    }

    public void setBreakFastCal(long breakFastCal) {
        this.breakFastCal = breakFastCal;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public long getLunchCal() {
        return lunchCal;
    }

    public void setLunchCal(long lunchCal) {
        this.lunchCal = lunchCal;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public long getDinnerCal() {
        return dinnerCal;
    }

    public void setDinnerCal(long dinnerCal) {
        this.dinnerCal = dinnerCal;
    }

    public void setWalking(long walking) {
        this.walking = walking;
    }

    public void setRunning(long running) {
        this.running = running;
    }

    public void setOther(long other) {
        this.other = other;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public float getWalking() {
        return walking;
    }

    public float getRunning() {
        return running;
    }

    public float getOther() {
        return other;
    }

    public String getResult() {
        return result;
    }
}
