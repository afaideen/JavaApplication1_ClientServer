/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 *
 * @author han
 */
public class MyEnergy implements Cloneable {
    private String timeString, sensorid, _id;
    private long datetime;
    private double energyTotal, energy1, energy1Total, energy2, energy2Total, energy3, energy3Total, CO2, todayCO2, costTotal, costLastWeek, costAveWeek, costAverageDaily;
    private double costLast24H, costToday;
    private long todaySampleStartT;
    private long todaySampleEndT;
    private Date date;
    private int type;
    private int month;
    private double costCurrentMonth;
    private double costAveCurrentMonth;
    private double costLastMonth;
    private double costAveLastMonth;
    private double costSaving;
    private double usageMonthly;
    private double averageUsageMonthly;
    private double usageLastMonth;
    private double usageAverageLastMonth;
    private double usageToday;
    private double costPreviousLastMonth;
    private double usagePreviousLastMonth;
    private double usageAverageDaily;
    private double energyActiveTotalMonthly;

    public MyEnergy(String id, String sensorId, double energyActive1, double totalEnergyActive1,double energyActive2, double totalEnergyActive2,
                        double energyActive3, double totalEnergyActive3, double totalCost, long datetime, String timeString) {
        this._id = id;
        this.sensorid = sensorId;
        this.energy1 = energyActive1;
        this.energy1Total = totalEnergyActive1;    
        this.energy2 = energyActive2;
        this.energy2Total = totalEnergyActive2; 
        this.energy3 = energyActive3;
        this.energy3Total = totalEnergyActive3; 
        this.costTotal = totalCost;
        this.datetime = datetime;
        this.timeString = timeString;
        this.costTotal = totalCost;
    }

    public MyEnergy() {
    }

    MyEnergy(String id, String sensorId, double energyActive1, double totalEnergyActive1,double energyActive2, double totalEnergyActive2,
                double energyActive3, double totalEnergyActive3, double totalCost, long datetime) {
        this._id = id;
        this.sensorid = sensorId;
        this.energy1 = energyActive1;
        this.energy1Total = totalEnergyActive1;    
        this.energy2 = energyActive2;
        this.energy2Total = totalEnergyActive2; 
        this.energy3 = energyActive3;
        this.energy3Total = totalEnergyActive3; 
        this.costTotal = totalCost;
        this.datetime = datetime;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public double getEnergy1() {
        return energy1;
    }

    public void setEnergy1(double energy1) {
        this.energy1 = energy1;
    }

    public double getEnergy1Total() {
        return energy1Total;
    }
    public void setEnergy1Total(double energy1Total) {
        this.energy1Total = energy1Total;
    }

    public double getEnergy2() {
        return energy2;
    }

    public void setEnergy2(double energy2) {
        this.energy2 = energy2;
    }

    public double getEnergy2Total() {
        return energy2Total;
    }

    public void setEnergy2Total(double energy2Total) {
        this.energy2Total = energy2Total;
    }

    public double getEnergy3() {
        return energy3;
    }

    public void setEnergy3(double energy3) {
        this.energy3 = energy3;
    }

    public double getEnergy3Total() {
        return energy3Total;
    }

    public void setEnergy3Total(double energy3Total) {
        this.energy3Total = energy3Total;
    }
    

    public double getTodayCO2() {
        return todayCO2;
    }

    public void setTodayCO2(double CO2) {
        this.todayCO2 = CO2;
    }

    public double getCO2() {
        return CO2;
    }

    public void setCO2(double CO2) {
        this.CO2 = CO2;
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

    public double getCostAveWeek() {
        return costAveWeek;
    }

    public void setCostAveWeek(double costAveWeek) {
        this.costAveWeek = costAveWeek;
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
//            jsonObject.put("todayCarbonDioxide", getTodayCO2());
            jsonObject.put("carbonDioxide", getCO2());  //total CO2 since install
            jsonObject.put("costToday", getCostToday());
            jsonObject.put("usageToday", usageToday);
            jsonObject.put("usageAverageDaily", usageAverageDaily);
            jsonObject.put("costAverageDaily", getCostAverageDaily());
//            jsonObject.put("costLast24H", getCostLast24H());
            jsonObject.put("costAverageWeek", getCostAveWeek());
            jsonObject.put("costLastWeek", getCostLastWeek());
            jsonObject.put("rank", "N/A");
            jsonObject.put("todaySampleStartT", todaySampleStartT);
            jsonObject.put("todaySampleEndT", todaySampleEndT);
            jsonObject.put("costCurrentMonth", costCurrentMonth);
            jsonObject.put("costAveCurrentMonth", costAveCurrentMonth);
            jsonObject.put("costLastMonth", costLastMonth);
            jsonObject.put("costAveLastMonth", costAveLastMonth);
            jsonObject.put("usageMonthly", usageMonthly);
            jsonObject.put("averageUsageMonthly", averageUsageMonthly);
            jsonObject.put("usageLastMonth", usageLastMonth);
            jsonObject.put("usageAverageLastMonth", usageAverageLastMonth);

//            jsonObject2.put("_id", get_id());
//            jsonObject2.put("payload", jsonObject);


            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }


    }

    void setCostToday(double totalCost) {
        this.costToday = totalCost;
    }

    public double getCostToday() {
        return costToday;
    }

    void setTodaySampleStartT(long tStart) {
        this.todaySampleStartT= tStart;
    }

    public long getTodaySampleStartT() {
        return todaySampleStartT;
    }

    public long getTodaySampleEndT() {
        return todaySampleEndT;
    }
    

    void setTodaySampleEndT(long tEnd) {
        this.todaySampleEndT= tEnd;
    }

    public double getEnergyTotal() {
        return energyTotal;
    }

    public void setEnergyTotal(double energyTotal) {
        this.energyTotal = energyTotal;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }


    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setCostCurrentMonth(double costCurrentMonth) {
        this.costCurrentMonth = costCurrentMonth;
    }

    public double getCostCurrentMonth() {
        return costCurrentMonth;
    }

    public void setCostAveCurrentMonth(double costAveCurrentMonth) {
        this.costAveCurrentMonth = costAveCurrentMonth;
    }

    public double getCostAveCurrentMonth() {
        return costAveCurrentMonth;
    }

    public void setCostLastMonth(double costLastMonth) {
        this.costLastMonth = costLastMonth;
    }

    public double getCostLastMonth() {
        return costLastMonth;
    }

    public void setCostAveLastMonth(double costAveLastMonth) {
        this.costAveLastMonth = costAveLastMonth;
    }

    public double getCostAveLastMonth() {
        return costAveLastMonth;
    }

    public void setCostSaving(double costSaving) {
        this.costSaving = costSaving;
    }

    public double getCostSaving() {
        return costSaving;
    }

    public void setUsageMonthly(double monthlyUsage) {
        this.usageMonthly = monthlyUsage;
    }

    public double getUsageMonthly() {
        return usageMonthly;
    }

    public void setAverageUsageMonthly(double averageMonthlyUsage) {
        this.averageUsageMonthly = averageMonthlyUsage;
    }

    public double getAverageUsageMonthly() {
        return averageUsageMonthly;
    }

    public void setUsageLastMonth(double usageLastMonth) {
        this.usageLastMonth = usageLastMonth;
    }

    public double getUsageLastMonth() {
        return usageLastMonth;
    }

    public void setUsageAverageLastMonth(double usageAverageLastMonth) {
        this.usageAverageLastMonth = usageAverageLastMonth;
    }

    public double getUsageAverageLastMonth() {
        return usageAverageLastMonth;
    }

    public void setUsageToday(double usageToday) {
        this.usageToday = usageToday;
    }

    public double getUsageToday() {
        return usageToday;
    }

    public void setCostPreviousLastMonth(double costPreviousLastMonth) {
        this.costPreviousLastMonth = costPreviousLastMonth;
    }

    public double getCostPreviousLastMonth() {
        return costPreviousLastMonth;
    }

    public void setUsagePreviousLastMonth(double usagePreviousLastMonth) {
        this.usagePreviousLastMonth = usagePreviousLastMonth;
    }

    public double getUsagePreviousLastMonth() {
        return usagePreviousLastMonth;
    }

    public void setUsageAverageDaily(double usageAverageDaily) {
        this.usageAverageDaily = usageAverageDaily;
    }

    public double getUsageAverageDaily() {
        return usageAverageDaily;
    }

    public void setEnergyActiveTotalMonthly(double energyActiveTotalMonthly) {
        this.energyActiveTotalMonthly = energyActiveTotalMonthly;
    }

    public double getEnergyActiveTotalMonthly() {
        return energyActiveTotalMonthly;
    }
}
