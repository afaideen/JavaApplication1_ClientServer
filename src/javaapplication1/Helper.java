/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author han
 */
public class Helper {
    public static double TarifCalculation(MyEnergy energy){
        
        double totalCost = 0;
        double balanceEnergy = 0;
        double totalEnergy = 0;

        totalEnergy = energy.getEnergy1Total() + energy.getEnergy2Total() + energy.getEnergy3Total();
    
    
        if(totalEnergy >= 200){

            totalCost = 200*0.218;
            balanceEnergy = totalEnergy - 200;
            if( balanceEnergy >= 100){
                totalCost = 100 * 0.334 + totalCost;
            }
            else{
                totalCost = balanceEnergy * 0.334 + totalCost;
            }

            balanceEnergy = balanceEnergy - 100;
            if( balanceEnergy >= 300){
                totalCost = 300 * 0.516 + totalCost;
            }
            else{
                totalCost = balanceEnergy * 0.516 + totalCost;
            }

            balanceEnergy = balanceEnergy - 300;
            if( balanceEnergy >= 300){
                totalCost = 300 * 0.546 + totalCost;
            }
            else{
                totalCost = balanceEnergy * 0.546 + totalCost;
            }

            balanceEnergy = balanceEnergy - 300;
            if( balanceEnergy > 0){
                totalCost = balanceEnergy * 0.571 + totalCost;
            }
        }
        else{
            totalCost = totalEnergy * 0.218;
        }
 
        return totalCost;
    }
    
    private static Calendar c;
    
    private static Date ConverDateFormat(Date dateToday, String sFormat) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(sFormat);
        String stringToday = sdf1.format(dateToday);
        try {
            dateToday = sdf1.parse(stringToday);
        } catch (ParseException ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateToday;
    }
    
    public static Date FindDateLastWeekStartDay(Date dateToday) {
        
        dateToday = ConverDateFormat(dateToday, "dd/M/yyyy");
        c = Calendar.getInstance();
        c.setTime(dateToday);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        return c.getTime();
    }
    public static Date FindDateLastWeekEndDay(Date dateToday) {
        FindDateLastWeekStartDay(dateToday);
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }
    
}
