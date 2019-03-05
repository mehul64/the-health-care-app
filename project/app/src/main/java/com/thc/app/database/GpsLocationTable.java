package com.thc.app.database;

import com.orm.SugarRecord;
import java.util.List;

public class GpsLocationTable extends SugarRecord<GpsLocationTable> {

    private String journeyId;
    private double longitude;
    private double latitude;
    private String dateTime;

    public GpsLocationTable() {
    }

    public GpsLocationTable(String id, double latitude, double longitude, String timestamp) {
        this.journeyId = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateTime = timestamp;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public static void addThisGpsLocation(String id, double latitude, double longitude, String dateTime) {
        GpsLocationTable table = new GpsLocationTable(id, latitude, longitude, dateTime);
//        table.save();
    }

    public static List<GpsLocationTable> getAllGpsLocation(String journeyId) {
        return GpsLocationTable.find(GpsLocationTable.class, "journey_id = ?", journeyId);
    }

    public static void deleteAllLocation(String journeyId) {
        GpsLocationTable.deleteAll(GpsLocationTable.class, "journey_id = ?", journeyId);
    }

}
