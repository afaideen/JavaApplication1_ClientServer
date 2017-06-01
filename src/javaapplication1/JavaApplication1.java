/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import javaapplication1.model.EnergyInfo;
import javaapplication1.model.MySensor;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javaapplication1.Helper.*;

/**
 *
 * @author han
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    private static String output;
    static String jsonData;
    static double totalEnergyActive1 = 0, totalEnergyActive2 = 0, totalEnergyActive3 = 0;
    static double totalCost = 0;
    static double[] totalCostWeek = new double[52];
    static int indexarray = 0;
    public static String[] sensorList;
    public static String urlAddress;
    static int indexCounter = 0, indexHopCounter = 0;
//    private static MyEnergy energy;
    private static double currentTotalEnergyActive1 = 0,currentTotalEnergyActive2 = 0,currentTotalEnergyActive3 = 0;
    private static int type;
    private static double costLastWeek, costCurrentMonth, costLastMonth;
    private static double costAverageWeek, costAverageCurrentMonth, costAverageLastMonth;
    private static double totalCO2;
    private static double costToday;
    private static double costAverageDaily;
    private static int dateTimeTodayStartIndex = 0;
    private static Date dateStart, dateEnd;
    private static Date dateToday;//, lastweekDateStart, lastweekDateEnd;
    private static  Date[] lastweekDateStart = new Date[52];
    private static  Date[] lastweekDateEnd = new Date[52];
    private static long tStart;
    private static long tEnd;
    private static int weekdataLastValidIndex;
    private static int todaydataLastValidIndex;
    private static double totHours;
    private static double totalEnergy;
    private static double[] weekEnergy, weekEnergyAggregate;
    private static List<MyEnergy> listEnergy;
    static double energyActive1 = 0;
    static double energyActive2 = 0;
    static double energyActive3 = 0;

    private static Date startDate;
    private static Helper helper = new Helper();
    private static List<MyData> listTotalData;
    private static int previousMonth, currentMonth;
    private static List<MyEnergy>[] listMonthEnergy;
    private static double costPreviousLastMonth;
    private static double costAveragePreviousLastMonth;
    private static double costSaving;
    private static List<MySensor> listSensor = new ArrayList<>();
    private static double usageAverageCurrentMonth;
    private static double usageAverageLastMonth;
    private static double todayEnergyUsage;

    public static void main(String[] args) throws CloneNotSupportedException {

        startDate = helper.SetDate("1/05/2017 00:00:00");//dd/MM/yyyy HH:mm:ss
        DateTime dateTime = helper.ConvertToJodaTime(startDate);

        if(helper.IsInThisMonth(dateTime)){
            // You have a hit.
            System.out.println("you hit");
        }

        List<String> list = Arrays.asList("a","ba",new String("a"));
        System.out.println(list);
        Collections.replaceAll(list, "a", "!!!!!");//easiest way to replace collection contents
        System.out.println(list);
        ArrayList<String> color_list;
        MyOperator<String> operator;

        color_list = new ArrayList<> ();
        operator = new MyOperator<> ();

        operator.varc1 = "red";

        // use add() method to add values in the list
        color_list.add("White");
        color_list.add("Black");
        color_list.add("Red");
        color_list.add("White");
        color_list.add("Yellow");
        color_list.add("White");
        System.out.println("List of Colors initially");
        System.out.println(color_list);
        // use add() method to add values in the list
        List<String> sample = new ArrayList<>();
        sample.add("Green");
        sample.add("Red");
        sample.add("White");
        sample.retainAll(color_list);
        System.out.println("After applying sample.retainAll()");
        System.out.println(sample);

        System.out.println("List of Colors");
        System.out.println(color_list);

        // Replace all colors with White color
        UnaryOperator<String> operator2 = pn->modifyName(pn);
        color_list.replaceAll(operator);
        System.out.println("Color list, after replacing all colors with White color :");
        System.out.println(color_list);

        sensorList = getSensorList();
        int size = sensorList.length;
        while(indexCounter < sensorList.length){
//        while(indexCounter < 2){
            try {
//                URL url = new URL("http://10.44.28.105/sensor_kafkaid?id=TM1101C263&points=8640");  //24hr 
//                URL url = new URL("http://10.44.28.105/sensor_kafkaid?id=TM1101C263&points=60480");     //7 days, a week
//                urlAddress = "http://10.44.28.105/sensor_kafkaid?id=" + sensorList[indexCounter++] + "&points=60480";
                indexHopCounter = 0;
                totalEnergy = 0;
                currentTotalEnergyActive1 = 0;
                currentTotalEnergyActive2 = 0;
                currentTotalEnergyActive3 = 0;
                weekEnergy = new double[52];
                weekEnergyAggregate = new double[52];
//                energy = new MyEnergy();
                listTotalData = new ArrayList<MyData>();
                listMonthEnergy = new List[12];

                while(indexHopCounter >= 0){    //week counter
//                while(indexHopCounter < 2){
                    sensorList[indexCounter] = "TM1101C263";//"TM11019EB9";//"TM110190EE";//"TM1101910E";//"TM11019026";
                    urlAddress = "http://10.44.28.105/sensor_data_by_week?sensor_id=" + sensorList[indexCounter] + "&hops=" + indexHopCounter; // last week version
//                    urlAddress = "http://10.44.28.105/sensor_data_by_week?sensor_id=" + "TM1101910E" + "&hops=" + indexHopCounter;
//                    urlAddress = "http://10.44.28.105/sensor_data_by_week?sensor_id=" + "TM11019026" + "&hops=" + indexHopCounter;
                    System.out.println(urlAddress);
                    URL url = new URL(urlAddress);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if (conn.getResponseCode() != 200) {
                                throw new RuntimeException("Failed : HTTP error code : "
                                                + conn.getResponseCode());
                        }
                    System.out.println("Read json data...");
                    String jsonArrayData = ReadData(conn);
                    if(jsonArrayData.equals("[]")){
//                        indexHopCounter = -1;
                        System.out.println("No json data exit...");
                        break;
                    }
                    if(indexHopCounter > 0) {
                        lastweekDateStart[indexHopCounter] = FindDateLastWeekStartDay(lastweekDateStart[indexHopCounter - 1]);
                        lastweekDateEnd[indexHopCounter] = FindDateLastWeekEndDay(lastweekDateStart[indexHopCounter - 1]);
                    }
                    else{
                        dateToday = new Date();
                        lastweekDateStart[0] = FindDateLastWeekStartDay(dateToday);
                        lastweekDateEnd[0] = FindDateLastWeekEndDay(dateToday);
                        dateTimeTodayStartIndex = 0;
                    }
                    System.out.println("Start extracting...");
                    List<MyData> listData = new ArrayList<>();
                    listData = Extract2(jsonArrayData);

                    System.out.println("list data size = " + listData.size());
                    if(listData.size()==0){
                        indexHopCounter++;
                        continue;
                    }

                    listTotalData.addAll(listData);
                    conn.disconnect();
                    indexHopCounter++;//next week
                }
                System.out.println("listTotalData before duplication removal is " + listTotalData.size());
                Collections.sort(listTotalData, new MyComparator());
                //check, if there is duplicate timestamp
                System.out.println("Checking duplication. Removing...");
                RemoveDuplication(listTotalData);//data with duplicated timestamp will be removed
                System.out.println("listTotalData size now after duplication removal is " + listTotalData.size());
                //check if there is still duplicate timestamp
                Collections.sort(listTotalData, new MyComparator());
                Validation(listTotalData);

                //Array List must be always intialized
                for (int i = 0; i < 12; i++) {
                    if(listMonthEnergy[i]==null)
                        listMonthEnergy[i] = new ArrayList<MyEnergy>();
                }
                energyActive1 = 0;
                energyActive2 = 0;
                energyActive3 = 0;
                totalEnergyActive1 = 0;
                totalEnergyActive2 = 0;
                totalEnergyActive3 = 0;
                for (MyData data : listTotalData) {
                    if(data.getType() == 1){
                        energyActive1 = Math.abs(Double.parseDouble(data.getPhase1().getActivepower())) * 10 / 3600 / 1000;
                        totalEnergyActive1 = totalEnergyActive1 + energyActive1;
                    }
                    else {
                        energyActive1 = Math.abs(Double.parseDouble(data.getPhase1().getActivepower())) * 10 / 3600 / 1000;
                        totalEnergyActive1 = totalEnergyActive1 + energyActive1;
                        energyActive2 = Math.abs(Double.parseDouble(data.getPhase2().getActivepower())) * 10 / 3600 / 1000;
                        totalEnergyActive2 = totalEnergyActive2 + energyActive2;
                        energyActive3 = Math.abs(Double.parseDouble(data.getPhase3().getActivepower())) * 10 / 3600 / 1000;
                        totalEnergyActive3 = totalEnergyActive2 + energyActive3;
                    }
                    totalEnergy = totalEnergyActive1 + totalEnergyActive2 + totalEnergyActive3;

                    DateTime jodaDate = helper.ConvertToJodaTime(new Date(data.getDateTime()*1000));
                    currentMonth = jodaDate.getMonthOfYear();
                    if(currentMonth!=previousMonth) {
                        System.out.println("data index:" + listTotalData.indexOf(data));
                        System.out.println("date:" + jodaDate);//data.getTimeString());
                        previousMonth = currentMonth;
                    }
                    MyEnergy energy = new MyEnergy();
                    energy = SetEnergy(data);

                    listMonthEnergy[currentMonth-1].add(energy);
                }

                MyEnergy energyCurrentMonth = listMonthEnergy[currentMonth-1].get(listMonthEnergy[currentMonth-1].size()-1);
                MyEnergy energyLastMonth = new MyEnergy();
                if(listMonthEnergy[currentMonth-1-1].size() > 0)
                    energyLastMonth = listMonthEnergy[currentMonth-1-1].get(listMonthEnergy[currentMonth-1-1].size()-1);
                MyEnergy energyPreviousLastMonth = new MyEnergy();
                if(listMonthEnergy[currentMonth-1-2].size() > 0)
                    energyPreviousLastMonth = listMonthEnergy[currentMonth-1-2].get(listMonthEnergy[currentMonth-1-2].size()-1);
                //Calculate monthly usage
                double monthlyEnergyUsage = energyCurrentMonth.getEnergyTotal() - energyLastMonth.getEnergyTotal();
                float totHoursCurrentMonth = (float) ((listMonthEnergy[currentMonth-1].size() * 10.0 / 60.0) / 60.0);//in hours
                costCurrentMonth = TarifCalculation(monthlyEnergyUsage);
                if(totHoursCurrentMonth > 0) {
                    usageAverageCurrentMonth = monthlyEnergyUsage/totHoursCurrentMonth;
                    costAverageCurrentMonth = costCurrentMonth / totHoursCurrentMonth;
                }
                //Calculate last month usage
                double lastMonthEnergyUsage = energyLastMonth.getEnergyTotal() - energyPreviousLastMonth.getEnergyTotal();
                float totHoursLastMonth = (float) ((listMonthEnergy[currentMonth-1-1].size() * 10.0 / 60.0) / 60.0);//in hours
                costLastMonth = TarifCalculation(lastMonthEnergyUsage);
                if(totHoursLastMonth > 0) {
                    usageAverageLastMonth = lastMonthEnergyUsage/totHoursLastMonth;
                    costAverageLastMonth = costLastMonth / totHoursLastMonth;
                }
                //Calculate previous last month
                double previousLastMonthEnergyUsage = 0;
                if(listMonthEnergy[currentMonth-1-3].size() > 0)
                    previousLastMonthEnergyUsage = energyPreviousLastMonth.getEnergyTotal() - listMonthEnergy[currentMonth-1-3].get(listMonthEnergy[currentMonth-1-3].size()-1).getEnergyTotal();

                float totHours = (float)((listMonthEnergy[currentMonth-1-3].size()*10.0/60.0) / 60.0);//in hours
                costPreviousLastMonth = TarifCalculation(previousLastMonthEnergyUsage);
                if(totHours > 0)
                    costAveragePreviousLastMonth = costPreviousLastMonth/totHours;
                costSaving = costLastMonth - costAveragePreviousLastMonth;
                //Calculate last week usage
                dateToday = new Date();
                lastweekDateStart[0] = FindDateLastWeekStartDay(dateToday);
                lastweekDateEnd[0] = FindDateLastWeekEndDay(dateToday);
                List<MyEnergy>listLastWeekUsage = new ArrayList<>();
                List<MyEnergy>listTodayUsage = new ArrayList<>();
                double totalEnergyPreviousWeek = 0;//, totalLastEnergyYesterday = 0;
                tStart = 0;
                for (MyEnergy energy : listMonthEnergy[currentMonth-1]) {
                    Date date = energy.getDate();
                    if(date.after(lastweekDateStart[0]) && date.before(lastweekDateEnd[0])){
                        listLastWeekUsage.add(energy);
                    }
                    else if(listLastWeekUsage.size() == 0){
                        totalEnergyPreviousWeek = energy.getEnergyTotal();
                    }
                    if(date.after(lastweekDateEnd[0])) {
                        if (DateUtils.isSameDay(date, Calendar.getInstance().getTime())) {
                            if(tStart == 0)
                                tStart = energy.getDatetime();
                            listTodayUsage.add(energy);
                        }
//                        else if (listTodayUsage.size() == 0) {
//                            totalLastEnergyYesterday = energy.getEnergyTotal();
//                        }
                    }
                }
                if(listTodayUsage.size() > 0)
                    tEnd = listTodayUsage.get(listTodayUsage.size()-1).getDatetime();
                //Calculate last week
                if(listLastWeekUsage.size() > 0) {
                    double lastWeekEnergyUsage = listLastWeekUsage.get(listLastWeekUsage.size() - 1).getEnergyTotal() - totalEnergyPreviousWeek;
                    float totHoursLastWeek = (float) ((listLastWeekUsage.size() * 10.0 / 60.0) / 60.0);//in hours
                    costLastWeek = TarifCalculation(lastWeekEnergyUsage);
                    costAverageWeek = costLastWeek / totHoursLastWeek;
                }
                //Calculate today usage
                if(listTodayUsage.size() > 0) {
                    todayEnergyUsage = listTodayUsage.get(listTodayUsage.size() - 1).getEnergyTotal() - (listTodayUsage.get(0).getEnergyTotal()
                                            - (listTodayUsage.get(0).getEnergy1()+listTodayUsage.get(0).getEnergy2()+listTodayUsage.get(0).getEnergy3()));//totalLastEnergyYesterday;
                    float totHoursToday = (float) ((listTodayUsage.size() * 10.0 / 60.0) / 60.0);//in hours
                    costToday = TarifCalculation(todayEnergyUsage);
                    costAverageDaily = costToday / totHoursToday;
                }
                //Calculate total cost
                totalCost = TarifCalculation(totalEnergy);
                totalCO2 = totalEnergy * 0.67552;

                String msg = "";
                MyEnergy energy = new MyEnergy();
                energy.setSensorid(sensorList[indexCounter]);
                energy.setDatetime(energyCurrentMonth.getDatetime());
                energy.setCostSaving(costSaving);
                energy.setCostTotal(totalCost);
                energy.setCostCurrentMonth(costCurrentMonth);
                energy.setUsageMonthly(monthlyEnergyUsage);
                energy.setAverageUsageMonthly(usageAverageCurrentMonth);
                energy.setCostAveCurrentMonth(costAverageCurrentMonth);
                //Last month
                energy.setCostLastMonth(costLastMonth);
                energy.setUsageLastMonth(lastMonthEnergyUsage);
                energy.setUsageAverageLastMonth(usageAverageLastMonth);
                energy.setCostAveLastMonth(costAverageLastMonth);
                //Previous Last Month
                energy.setCostPreviousLastMonth(costPreviousLastMonth);
                energy.setUsagePreviousLastMonth(previousLastMonthEnergyUsage);
                //Last week
                energy.setCostLastWeek(costLastWeek);
                energy.setCostAveWeek(costAverageWeek);
                energy.setCostToday(costToday);
                energy.setUsageToday(todayEnergyUsage);
                energy.setCostAverageDaily(costAverageDaily);
                energy.setCO2(totalCO2);
                energy.setTodaySampleStartT(tStart);
                energy.setTodaySampleEndT(tEnd);
                energy.setEnergyTotal(totalEnergy);
                MySensor mySensor = new MySensor();
                mySensor.setEnergy(energy);
                mySensor.setId(energy.getSensorid());
                listSensor.add(mySensor);

                System.out.println("Sensor: " + sensorList[indexCounter] + " Total Energy: " + totalEnergy);
                msg = energy.toJSON().toString();
                System.out.println("data sent: " + msg);

                //http://stackoverflow.com/questions/21974407/how-to-stream-a-json-object-to-a-httpurlconnection-post-request            
                URL urlpost = new URL("http://10.44.28.105/dashboard");
                HttpURLConnection httpCon = (HttpURLConnection) urlpost.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);
                httpCon.setRequestProperty( "Content-Type", "application/json" );
                httpCon.setRequestProperty("charset", "utf-8");
                httpCon.setFixedLengthStreamingMode(msg.getBytes().length); //this line is a must!
                 httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect(); // Note the connect() here

                // Send POST output. Commented code below also useable
                DataOutputStream printout = new DataOutputStream(httpCon.getOutputStream());
                printout.writeBytes(msg);
                printout.flush ();
                printout.close ();

            } catch (MalformedURLException ex) {
                Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
            }
            indexCounter++; //next sensor
        }
        if(listSensor.size() > 0){
            Collections.sort(listSensor, new MyComparatorSaving());
            for (MySensor sensor : listSensor) {
                System.out.println("Sensor:" + sensor.getId() + " Saving:" + sensor.getEnergy().getCostSaving());

            }
            JSONObject jsonObject= new JSONObject();
            JSONObject jsonSensor[]= new JSONObject[listSensor.size()];
            JSONObject jsonRank = new JSONObject();
            MySensor sensor = new MySensor();
            for (int i = 0; i < listSensor.size(); i++) {
                sensor = listSensor.get(i);
                jsonObject= new JSONObject();
                jsonObject.put("sensorId", sensor.getId());
                jsonObject.put("saving", sensor.getEnergy().getCostSaving());
                jsonObject.put("rank", i+1);
                jsonObject.put("costLastMonth", sensor.getEnergy().getCostLastMonth());
                jsonObject.put("usageLastMonth", sensor.getEnergy().getUsageLastMonth());
                jsonObject.put("costPreviousLastMonth", sensor.getEnergy().getCostPreviousLastMonth());
                jsonObject.put("usagePreviousLastMonth", sensor.getEnergy().getUsagePreviousLastMonth());
                String labelSensor = "sensor[" + i + "]";
                jsonSensor[i] = new JSONObject();//create object
                jsonSensor[i].put(labelSensor, jsonObject);
            }
            jsonRank.put("rank", jsonSensor);
            String msg = jsonRank.toString();
            System.out.println("data sent: " + msg);
            try {
                URL urlpost = new URL("http://10.44.28.105/dashboard");
                HttpURLConnection httpCon = (HttpURLConnection) urlpost.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);
                httpCon.setRequestProperty( "Content-Type", "application/json" );
                httpCon.setRequestProperty("charset", "utf-8");
                httpCon.setFixedLengthStreamingMode(msg.getBytes().length); //this line is a must!
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect(); // Note the connect() here

                // Send POST output. Commented code below also useable
                DataOutputStream printout = new DataOutputStream(httpCon.getOutputStream());
                printout.writeBytes(msg);
                printout.flush ();
                printout.close ();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static void Validation(List<MyData> listTotalData) {
        int index = 0, count = 0;
        for (MyData myData : listTotalData) {
            index = listTotalData.indexOf(myData);
            if(index > 0) {
                if(myData.getDateTime() == listTotalData.get(index-1).getDateTime()){
//                    System.out.println("data with index " + index + " duplicated with previous index, id is " + myData.getId());
                    count++;
                }
                else if(myData.getDateTime() < listTotalData.get(index-1).getDateTime()){
                    System.out.println("Wrong sort");
                }
            }
        }
        System.out.println("Duplication detected: " + count);
    }

    private static void RemoveDuplication(List<MyData> listData) {
        int index = 0, count = 0;
        Iterator<MyData> iter = listData.iterator();
        while (iter.hasNext()){
            MyData myData = iter.next();
            index = listData.indexOf(myData);
            if(index != 0) {
                if(myData.getDateTime() == listData.get(index-1).getDateTime()){
//                    System.out.println("data with index " + index + " duplicated with previous index, id is " + myData.getId());
                    iter.remove();
                    count++;
                }
            }
        }
        System.out.println("Duplication detected: " + count);
    }

    private static MyEnergy SetEnergy(MyData data) {
        MyEnergy energy = new MyEnergy();
        energy.setSensorid(data.getSensorId());
        energy.set_id(data.getId());
        energy.setDatetime(data.getDateTime()*1000);
        Date date = new Date(data.getDateTime()*1000);
        energy.setDate(date);
        energy.setTimeString(data.getTimeString());
        energy.setType(data.getType());
        energy.setEnergy1(energyActive1);
        energy.setEnergy1Total(totalEnergyActive1);
        energy.setEnergy2(energyActive2);
        energy.setEnergy2Total(totalEnergyActive2);
        energy.setEnergy3(energyActive3);
        energy.setEnergy3Total(totalEnergyActive3);
        energy.setEnergyTotal(totalEnergy);
        return energy;
    }

    private static String modifyName(String pn) {
        pn = pn.concat(" test here");
        return pn;
    }

    private static MyEnergy monthlyEnergy = new MyEnergy();
    private static List<MyEnergy> listMonthlyEnergy = new ArrayList<>();
    private static EnergyInfo[] monthInfo = new EnergyInfo[12];
//    private static void ProcessDataMonthly(List<MyData> listData) {
//        int month = 0;
//        for (int i = 0; i < 12; i++) {
//            if(monthInfo[i]==null)
//                monthInfo[i] = new EnergyInfo();
//        }
//        for(MyData data:listData){
//            long datetime = data.getDateTime()*1000;
//            Date date = new Date(datetime);
//            DateTime jodaDate = helper.ConvertToJodaTime(date);
//            month = jodaDate.getMonthOfYear();
//            SimpleDateFormat sdf  = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//            String timeString = sdf.format(date);
//
//            CalculateEnergy(data);
//            if(monthlyEnergy.getMonth() != month){
//                listMonthlyEnergy.clear();
//            }
//            monthlyEnergy.setMonth(month);
//            monthlyEnergy.setSensorid(data.getSensorId());
//            monthlyEnergy.set_id(data.getId());
//            monthlyEnergy.setType(data.getType());
//            monthlyEnergy.setEnergy1(energyActive1);
//            monthlyEnergy.setEnergy1Total(totalEnergyActive1);
//            monthlyEnergy.setEnergy2(energyActive2);
//            monthlyEnergy.setEnergy2Total(totalEnergyActive2);
//            monthlyEnergy.setEnergy3(energyActive3);
//            monthlyEnergy.setEnergy3Total(totalEnergyActive3);
//
//            monthlyEnergy.setEnergyTotal(totalEnergyActive1 + totalEnergyActive2 + totalEnergyActive3);
//            monthlyEnergy.setDatetime(datetime);
//            monthlyEnergy.setDate(date);
//            monthlyEnergy.setTimeString(timeString);
//            listMonthlyEnergy.add(monthlyEnergy);
//
//            monthInfo[month].setlistMonthlyEnergy(listMonthlyEnergy);
//
//        }
//
//    }

    private static void ProcessDataWeekly(List<MyData> listData) {
        for(MyData data:listData){

            type = data.getType();
            long datetime = data.getDateTime()*1000;
            Date date = new Date(datetime);
            if(indexHopCounter == 0){
                if(!DateUtils.isSameDay(date, Calendar.getInstance().getTime())){
//                    System.out.println("DataIndex " + listData.indexOf(data) + " has date " + date);
                    continue;
                }
                else{
                    if(dateTimeTodayStartIndex == 0)
                        dateTimeTodayStartIndex = listData.indexOf(data);
                    todaydataLastValidIndex = listData.indexOf(data);
                }
            }
            else {
                if (!date.after(lastweekDateStart[indexHopCounter-1]) || !date.before(lastweekDateEnd[indexHopCounter-1])) {
                    System.out.println(lastweekDateStart[indexHopCounter-1] + " - " + lastweekDateEnd[indexHopCounter-1] + " DataIndex " + listData.indexOf(data) + " has date " + date);
                    continue;
                }
                weekdataLastValidIndex = listData.indexOf(data);

            }
            CalculateEnergy(data);

//                        String timeString = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(new Date(datetime*1000));
            SimpleDateFormat sdf  = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            String timeString = sdf.format(date);
            MyEnergy tempenergy = new MyEnergy(data.getId(), data.getSensorId(), energyActive1, totalEnergyActive1,
                    energyActive2, totalEnergyActive2, energyActive3, totalEnergyActive3, totalCost, datetime, timeString);
            listEnergy.add(tempenergy);
        }

        if(indexHopCounter == 0){
            tStart = listData.get(dateTimeTodayStartIndex).getDateTime()*1000;
            dateStart = new Date(tStart);
            tEnd = listData.get(todaydataLastValidIndex).getDateTime()*1000;
            dateEnd = new Date(tEnd);
            totHours = ((tEnd/1000.0-tStart/1000.0)/60)/60;
            System.out.println("Today, " + dateStart + " - " + dateEnd + ". Start index is " + dateTimeTodayStartIndex + ". Last valid index is " + todaydataLastValidIndex);
            System.out.println("Today duration hours is " + ((tEnd/1000.0-tStart/1000.0)/60)/60);

        }
        else {
            if(indexHopCounter == 1)
                System.out.println("Last week is: " + lastweekDateStart[indexHopCounter-1] + " - " + lastweekDateEnd[indexHopCounter-1]);
            else
                System.out.println("Week is: " + lastweekDateStart[indexHopCounter-1] + " - " + lastweekDateEnd[indexHopCounter-1]);
            tStart = listData.get(0).getDateTime()*1000;
            dateStart = new Date(tStart);
            tEnd = listData.get(weekdataLastValidIndex).getDateTime()*1000;
            dateEnd = new Date(tEnd);
            totHours = ((tEnd/1000.0-tStart/1000.0)/60)/60;
            System.out.println("Week, " + dateStart + " - " + dateEnd + ". Last valid index is " + weekdataLastValidIndex);
            System.out.println("Week duration hours is " + ((tEnd/1000.0-tStart/1000.0)/60)/60);
        }

        int lastindex = listEnergy.size() - 1;
        MyEnergy energy = listEnergy.get(lastindex);
        currentTotalEnergyActive1 = currentTotalEnergyActive1 + energy.getEnergy1Total();
        currentTotalEnergyActive2 = currentTotalEnergyActive2 + energy.getEnergy2Total();
        currentTotalEnergyActive3 = currentTotalEnergyActive3 + energy.getEnergy3Total();
        totalEnergy = currentTotalEnergyActive1 + currentTotalEnergyActive2 + currentTotalEnergyActive3;
        System.out.println("listEnergy size: " + listEnergy.size() + " total energy: " + totalEnergy );
        totalCostWeek[indexHopCounter] = TarifCalculation(totalEnergy);
        totalCost += totalCostWeek[indexHopCounter];
        totalCO2 = totalEnergy * 0.67552;

        if(indexHopCounter == 0){
            costToday = totalCostWeek[0];//energy.getCostToday();
            costAverageDaily = energy.getCostToday()/totHours;//energy.getCostAverageDaily();
            System.out.println("costToday: "+ costToday + " costAverageDaily: " + costAverageDaily);
        }
        else if(indexHopCounter == 1){ //week 1
            costLastWeek = totalCostWeek[1];//energy.getCostLastWeek();
            costAverageWeek = costLastWeek/totHours;//energy.getCostAveWeek();
            System.out.println("costLastWeek: "+ costLastWeek + " costAverageWeek: " + costAverageWeek);

        }

        weekEnergy[indexHopCounter] = totalEnergyActive1 + totalEnergyActive2 + totalEnergyActive3;
        if(indexHopCounter == 0)
            weekEnergyAggregate[indexHopCounter] = weekEnergy[indexHopCounter];
        else {
            weekEnergyAggregate[indexHopCounter] = weekEnergy[indexHopCounter] + weekEnergyAggregate[indexHopCounter-1];
        }
    }

    private static void CalculateEnergy(MyData data) {

        if(data.getType() == 1){
            energyActive1 = Math.abs(Double.parseDouble(data.getPhase1().getActivepower())) * 10/3600/1000;
            totalEnergyActive1 = totalEnergyActive1 + energyActive1;
        }
        else{
            energyActive1 = Math.abs(Double.parseDouble(data.getPhase1().getActivepower())) * 10/3600/1000;
            totalEnergyActive1 = totalEnergyActive1 + energyActive1;

            energyActive2 = Math.abs(Double.parseDouble(data.getPhase2().getActivepower())) * 10/3600/1000;
            totalEnergyActive2 = totalEnergyActive2 + energyActive2;

            energyActive3 = Math.abs(Double.parseDouble(data.getPhase3().getActivepower())) * 10/3600/1000;
            totalEnergyActive3 = totalEnergyActive3 + energyActive3;

        }

    }

    private static String ReadData(HttpURLConnection conn) throws IOException {
        String jsonData = "";
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

//        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
//            System.out.println(output);
            jsonData = output;
        }
        return jsonData;
    }

    private static List<MyData> Extract2(String jsonData)  {
        MyData data = new MyData();
        List<MyData> listMyData = new ArrayList<>();
        try{
            JSONArray objArray = new JSONArray(jsonData);   //when internet goes slow always crash here
            System.out.println("Just Wait...");
            for(int i = 0; i < objArray.length(); i++){
//                System.out.println(i + "");
                data = new MyData();
                JSONObject obj = (JSONObject) objArray.get(i);//new JSONObject(jsonData); 
                data.setDateTime(Long.parseLong(obj.getString("dateTime")));
                Date date = new Date(data.getDateTime()*1000);
                if (i == 0) {
                    System.out.println("At index 0, data has date " + date);
                }
                if(indexHopCounter > 0) {

                    if (!date.after(lastweekDateStart[indexHopCounter - 1]) || !date.before(lastweekDateEnd[indexHopCounter - 1])) {
//                        System.out.println(lastweekDateStart[indexHopCounter - 1] + " - " + lastweekDateEnd[indexHopCounter - 1] + " DataIndex " + i + " has date " + date);

                    }
                }
                data.setId(obj.getString("_id"));
                data.setCounter(Integer.parseInt(obj.getString("counter")));

//                data.setTimeString(data.getDateTime());
                data.setT(obj.getLong("t"));
                data.setSensorId(obj.getString("sensorId"));
                data.setType(Integer.parseInt(obj.getString("type")));

                JSONObject childrenPhase1 = new JSONObject();
                childrenPhase1 = obj.getJSONObject("phase1");
                PowerPhase phase1 = new PowerPhase();
                phase1.setActivepower(childrenPhase1.getString("activepower"));
                phase1.setApparentpower(childrenPhase1.getString("apparentpower"));
                phase1.setCurrent(childrenPhase1.getString("current"));
                phase1.setVoltage(childrenPhase1.getString("voltage"));
                phase1.setPowerfactor(childrenPhase1.getString("powerfactor"));
                phase1.setDeviceId(childrenPhase1.getString("deviceId"));
                data.setPhase1(phase1);
//                System.out.println("dataType: " + data.getType());
                if(data.getType()==1 || data.getType()==2){
                    listMyData.add(data);
                    continue;
                }


                JSONObject childrenPhase2 = new JSONObject();            
                childrenPhase2 = obj.getJSONObject("phase2");
                PowerPhase phase2 = new PowerPhase();
                phase2.setActivepower(childrenPhase2.getString("activepower"));
                phase2.setApparentpower(childrenPhase2.getString("apparentpower"));
                phase2.setCurrent(childrenPhase2.getString("current"));
                phase2.setVoltage(childrenPhase2.getString("voltage"));
                phase2.setPowerfactor(childrenPhase2.getString("powerfactor"));
                phase2.setDeviceId(childrenPhase2.getString("deviceId"));
                data.setPhase2(phase2);


                JSONObject childrenPhase3 = new JSONObject();
                childrenPhase3 = obj.getJSONObject("phase3");
                PowerPhase phase3 = new PowerPhase();
                phase3.setActivepower(childrenPhase3.getString("activepower"));
                phase3.setApparentpower(childrenPhase3.getString("apparentpower"));
                phase3.setCurrent(childrenPhase3.getString("current"));
                phase3.setVoltage(childrenPhase3.getString("voltage"));
                phase3.setPowerfactor(childrenPhase3.getString("powerfactor"));
                phase3.setDeviceId(childrenPhase3.getString("deviceId"));
                data.setPhase3(phase3);
                listMyData.add(data);

            }
        }catch (JSONException e){
            System.out.println(e + "dataType: " + data.getType());
        }
         
        
        return listMyData;
    }

    
    public static String[] getSensorList() {
        try {
            // URL of connection
            URL url = new URL("http://10.44.28.105/sensors_status");
            // sets the connection
            // with GET type
            // and Accepts a JSON object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
            // get JSON response object
            String jsonData = "";
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            if ((jsonData = br.readLine()) == null) {
                throw new RuntimeException("NO JSON OUTPUT");
            }
            
            // create a JSON array
            JSONObject JSONData = new JSONObject(jsonData);
            conn.disconnect();
            
            // get the list of devices in a JSONArray
            JSONArray sensorsList = JSONData.getJSONArray("list_of_devices");
            String[] output = new String[sensorsList.length()];
            
            // loop each sensor for the sensor ID
            for (int x = 0; x < sensorsList.length(); x++) {
                output[x] = sensorsList.getJSONObject(x).getString("sensorId");
                System.out.println(output[x]);
            }
            return output;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }


    static class MyOperator<String> implements UnaryOperator<String> {
        private String varc1;
//        T varc1;
//        public T apply(T varc){
//            return varc1;
//        }

        @Override
        public String apply(String s) {
            return varc1;
        }
    }

    public static class MyComparator implements Comparator<MyData> {
        @Override
        public int compare(MyData o1, MyData o2) {
            if (o1.getDateTime() < o2.getDateTime()) {
                return -1;
            } else if (o1.getDateTime() > o2.getDateTime()) {
                return 1;
            }
            return 0;
        }


    }

    private static class MyComparatorSaving implements Comparator<MySensor>{
        @Override
        public int compare(MySensor o1, MySensor o2) {
            if (o1.getEnergy().getCostSaving() > o2.getEnergy().getCostSaving()) {
                return -1;
            } else if (o1.getEnergy().getCostSaving() < o2.getEnergy().getCostSaving()) {
                return 1;
            }
            return 0;
        }
    }
}
