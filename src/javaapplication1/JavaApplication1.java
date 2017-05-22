/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.time.temporal.TemporalQueries.zone;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javaapplication1.Helper.FindDateLastWeekEndDay;
import static javaapplication1.Helper.FindDateLastWeekStartDay;
import static javaapplication1.Helper.TarifCalculation;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author han
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    private static String output;
    static String jsonData, jsonArrayData;
    static double totalEnergyActive1 = 0, totalEnergyActive2 = 0, totalEnergyActive3 = 0;
    static double totalCost = 0;
    static double[] totalCostWeek = new double[52];
    static int indexarray = 0;
    public static String[] sensorList;
    public static String urlAddress;
    static int indexCounter = 0, indexHopCounter = 0;
    private static MyEnergy energy;
    private static double currentTotalEnergyActive1 = 0,currentTotalEnergyActive2 = 0,currentTotalEnergyActive3 = 0;
    private static int type;
    private static double costLastWeek;
    private static double costAverageWeek;
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

    private static Date startDate;
    private static Helper helper = new Helper();

    public static void main(String[] args) throws CloneNotSupportedException {
//        startDate = helper.SetDate("1/05/2017 00:00:00");//dd/MM/yyyy HH:mm:ss
//        DateTime dateTime = helper.ConvertToJodaTime(startDate);
//        if(helper.IsInThisMonth(dateTime)){
//            // You have a hit.
//            System.out.println("you hit");
//        }
        
        sensorList = getSensorList();
        int size = sensorList.length;
        while(indexCounter < sensorList.length){
           
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
                energy = new MyEnergy();


                
                while(indexHopCounter >= 0){
//                while(indexHopCounter < 2){
//                    urlAddress = "http://10.44.28.105/sensor_data_by_week?sensor_id=" + sensorList[indexCounter] + "&hops=" + indexHopCounter; // last week version
                    urlAddress = "http://10.44.28.105/sensor_data_by_week?sensor_id=" + "TM1101910E" + "&hops=" + indexHopCounter;
                    System.out.println(urlAddress);
                    URL url = new URL(urlAddress);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if (conn.getResponseCode() != 200) {
                                throw new RuntimeException("Failed : HTTP error code : "
                                                + conn.getResponseCode());
                        }

                    jsonArrayData = ReadData(conn);
                    if(jsonArrayData.equals("[]")){
                        indexHopCounter = -1;
                        break;
                    }

                    List<MyData> listData = Extract2(jsonArrayData);
                    System.out.println("list data size = " + listData.size());
                    if(listData.size()==0){
                        indexHopCounter++;
                        continue;
                    }
                    listEnergy = new ArrayList<>();
                    conn.disconnect();
                    double energyActive1 = 0, energyActive2 = 0, energyActive3 = 0;
                    totalEnergyActive1 = 0;
                    totalEnergyActive2 = 0;
                    totalEnergyActive3 = 0;
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


                    for(MyData data:listData){
                        
                        type = data.getType();
                        long datetime = data.getDateTime()*1000;
                        Date date = new Date(datetime);
                        if(indexHopCounter == 0){
                            if(!DateUtils.isSameDay(date, Calendar.getInstance().getTime())){
//                                System.out.println("DataIndex " + listData.indexOf(data) + " has date " + date);
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
//                                System.out.println(lastweekDateStart + " - " + lastweekDateEnd + " DataIndex " + listData.indexOf(data) + " has date " + date);
                                continue;
                            }
                            weekdataLastValidIndex = listData.indexOf(data);
                            
                        }
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
                    energy  = listEnergy.get(lastindex);
                    currentTotalEnergyActive1 = currentTotalEnergyActive1 + energy.getEnergy1Total();
                    currentTotalEnergyActive2 = currentTotalEnergyActive2 + energy.getEnergy2Total();
                    currentTotalEnergyActive3 = currentTotalEnergyActive3 + energy.getEnergy3Total();
                    totalEnergy = currentTotalEnergyActive1 + currentTotalEnergyActive2 + currentTotalEnergyActive3;
                    System.out.println("listEnergy size: " + listEnergy.size() + " total energy: " + totalEnergy );
                    totalCostWeek[indexHopCounter] = TarifCalculation(energy);
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

                    indexHopCounter++;//next week
                }
                
                
                String msg = "";
                energy.setCostTotal(totalCost);
                energy.setCostLastWeek(costLastWeek);
                energy.setCostAveWeek(costAverageWeek);
                energy.setCostToday(costToday);
                energy.setCostAverageDaily(costAverageDaily);
                energy.setCO2(totalCO2);
                energy.setTodaySampleStartT(tStart);
                energy.setTodaySampleEndT(tEnd);
                energy.setEnergyTotal(totalEnergy);
                System.out.println("Sensor: " + sensorList[indexCounter] + " Total Energy: " + totalEnergy);
                msg = energy.toJSON().toString();
                System.out.println("data sent: " + msg);
//                msg = "[" + msg + "]";

                //http://stackoverflow.com/questions/21974407/how-to-stream-a-json-object-to-a-httpurlconnection-post-request            
                URL urlpost = new URL("http://10.44.28.105/dashboard");
                HttpURLConnection httpCon = (HttpURLConnection) urlpost.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);
                httpCon.setRequestProperty( "Content-Type", "application/json" );
                httpCon.setRequestProperty("charset", "utf-8");
                httpCon.setFixedLengthStreamingMode(msg.getBytes().length); //this line is a must!
    //                httpCon.setRequestProperty("Content-Length", "" + Integer.toString(msg.getBytes().length));//Integer.toString(urlpost.getBytes().length));        //something wrong with this line
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestMethod("POST");
                httpCon.connect(); // Note the connect() here

                // Send POST output. Commented code below also useable
                DataOutputStream printout = new DataOutputStream(httpCon.getOutputStream());
    //            printout.writeBytes(URLEncoder.encode(msg,"UTF-8"));
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
        
        
    }

    private static String ReadData(HttpURLConnection conn) throws IOException {
        String jsonData = "";
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
//            System.out.println(output);
            jsonData = output;
        }
        return jsonData;
    }
    private static MyData data;
    private static List<MyData> Extract2(String jsonData) throws JSONException {
        
        List<MyData> listMyData = new ArrayList<>();
        try{
            JSONArray objArray = new JSONArray(jsonData);
            for(int i = 0; i < objArray.length(); i++){
//                System.out.println(i + "");
               
                JSONObject obj = (JSONObject) objArray.get(i);//new JSONObject(jsonData); 
                data = new MyData();
                data.setId(obj.getString("_id"));
                data.setCounter(Integer.parseInt(obj.getString("counter")));
                data.setDateTime(Long.parseLong(obj.getString("dateTime")));
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
                if(data.getType()==1){
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
    
    private static MyData Extract(String jsonData) throws JSONException {
        String regx = "[]";
        char[] ca = regx.toCharArray();
        for (char c : ca) {
            jsonData = jsonData.replace(""+c, "");
        }
 

        JSONObject obj = new JSONObject(jsonData);  
        MyData data = new MyData();
        data.setId(obj.getString("_id"));
        data.setCounter(Integer.parseInt(obj.getString("counter")));
        data.setDateTime(Long.parseLong(obj.getString("dateTime")));
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
        return data;
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

    
    private void basic(){
        double first_number = 10.5, second_number, third_number, answer;
        second_number = 20.8;
        third_number = 2.7;
        answer = first_number + second_number;
        // TODO code application logic here
        System.out.println( "My First Project" );
        System.out.println( "First number = " + first_number );
        System.out.println("Addition Total = " + answer );
        
        answer = first_number - (second_number + third_number);
        System.out.println("Total = " + answer );
        
        String first_name = "William";
        String family_name = "Shakespeare";
        System.out.println( first_name + " " + family_name );
        
        Scanner user_input = new Scanner( System.in );
        
        System.out.print("Enter your first name: ");
        first_name = user_input.next( );
        
        System.out.print("Enter your family name: ");
        family_name = user_input.next( );
        
        String full_name;
        full_name = first_name + " " + family_name;
        System.out.println("You are " + full_name);
        
        int[ ] aryNums = { 24, 6, 47, 35, 2, 14 };

        int i;
        int arrayTotal = 0;
        int average = 0;

        for (i=0; i < aryNums.length; i++) {
            arrayTotal = arrayTotal + aryNums[ i ];
        }

        average = arrayTotal / aryNums.length;
        System.out.println("total: " + average);
    }
    
}
