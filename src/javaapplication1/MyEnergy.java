/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author han
 */
class MyEnergy {
    private String timeString, sensorid, _id;
    private long datetime;
    private double energy, energyTotal, CO2, costTotal, costLastWeek, costAverageDaily;
    private double costLast24H;

    public MyEnergy(String id, String sensorId, double energyActive, double totalEnergyActive, double totalCost, long datetime, String timeString) {
        this._id = id;
        this.sensorid = sensorId;
        this.energy = energyActive;
        this.energyTotal = totalEnergyActive;
        this.datetime = datetime;
        this.timeString = timeString;
        this.costTotal = totalCost;
    }

    public MyEnergy() {
    }

    MyEnergy(String id, String sensorId, double energyActive, double totalEnergyActive, double totalCost, long datetime) {
        this._id = id;
        this.sensorid = sensorId;
        this.energy = energyActive;
        this.energyTotal = totalEnergyActive;      
        this.costTotal = totalCost;
        this.datetime = datetime;
    }

    public String get_id() {
        return _id;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getEnergyTotal() {
        return energyTotal;
    }

    public double getCO2() {
        return CO2;
    }

    public void setCO2(double CO2) {
        this.CO2 = CO2;
    }

    public void setEnergyTotal(double energyTotal) {
        this.energyTotal = energyTotal;
    }

    public double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(double costTotal) {
        this.costTotal = costTotal;
    }

    public double getCostLastWeek() {
        return costLastWeek;
    }

    public void setCostLastWeek(double costLastWeek) {
        this.costLastWeek = costLastWeek;
    }

    public double getCostAverageDaily() {
        return costAverageDaily;
    }

    public void setCostAverageDaily(double costAverageDaily) {
        this.costAverageDaily = costAverageDaily;
    }

    public double getCostLast24H() {
        return costLast24H;
    }

    public void setCostLast24H(double costLast24H) {
        this.costLast24H = costLast24H;
    }

    
    
    
    public String getSensorid() {
        return sensorid;
    }

    public void setSensorid(String sensorid) {
        this.sensorid = sensorid;
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
//        JSONObject payload= new JSONObject();
//        JSONObject jsonObject2= new JSONObject();
        try {
            jsonObject.put("dateTime", getDatetime());
            jsonObject.put("sensorId", getSensorid());
            jsonObject.put("carbonDioxide", getCO2());
            jsonObject.put("costToday", getCostTotal());
            jsonObject.put("costAverageDaily", getCostAverageDaily());
            jsonObject.put("costLast24H", getCostLast24H());
            jsonObject.put("costAverageWeek", getCostLastWeek());
            jsonObject.put("costLastWeek", getCostLastWeek());
            jsonObject.put("rank", 1234);

//            jsonObject2.put("_id", get_id());
//            jsonObject2.put("payload", jsonObject);


            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }


    }

   
    
}
