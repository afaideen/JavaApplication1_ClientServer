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
    private double energy, energyTotal, costTotal, totalCostLastWeek;

    public MyEnergy(String id, String sensorId, double energyActive, double totalEnergyActive, double totalCost, long datetime) {
        this._id = id;
        this.sensorid = sensorId;
        this.energy = energyActive;
        this.energyTotal = totalEnergyActive;
        this.datetime = datetime;
//        this.timeString = timeString;
        this.costTotal = totalCost;
    }

    public MyEnergy() {
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

    public void setEnergyTotal(double energyTotal) {
        this.energyTotal = energyTotal;
    }

    public double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(double costTotal) {
        this.costTotal = costTotal;
    }

    public String getSensorid() {
        return sensorid;
    }

    public void setSensorid(String sensorid) {
        this.sensorid = sensorid;
    }
    
    void setCostLastWeek(double totalCost) {
        this.totalCostLastWeek = totalCost;
    }

    public double getTotalCostLastWeek() {
        return totalCostLastWeek;
    }
    

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
//        JSONObject payload= new JSONObject();
        JSONObject jsonObject2= new JSONObject();
        try {
            jsonObject.put("dateTime", getDatetime());
            jsonObject.put("sensorId", getSensorid());
            jsonObject.put("carbonDioxide", 123);
            jsonObject.put("costToday", 1234);
            jsonObject.put("costAverageDaily", 1234);
            jsonObject.put("costAverageWeek", 1234);
            jsonObject.put("costLastWeek", 1234);
            jsonObject.put("rank", 1234);
//            payload.putOpt("payload", jsonObject);
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
