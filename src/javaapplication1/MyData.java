/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author han
 */
class MyData {

    private String id;
    private int counter;
    private long dateTime;
    private long t;
    private String sensorId;
    private int type;
    private PowerPhase phase1;
    private PowerPhase phase2;
    private PowerPhase phase3;
    private String timeString;

    public MyData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public PowerPhase getPhase1() {
        return phase1;
    }

    public void setPhase1(PowerPhase phase1) {
        this.phase1 = phase1;
    }

    public PowerPhase getPhase2() {
        return phase2;
    }

    public void setPhase2(PowerPhase phase2) {
        this.phase2 = phase2;
    }

    public PowerPhase getPhase3() {
        return phase3;
    }

    public void setPhase3(PowerPhase phase3) {
        this.phase3 = phase3;
    }

    public void setTimeString(long timeLong) {
        Date date = new Date(timeLong*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String time = sdf.format(date);
        this.timeString = time;

    }

    public String getTimeString() {
        return timeString;
    }
}
