/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author han
 */
class PowerPhase {
    private String activepower;
    private String apparentpower;
    private String current;
    private String voltage;
    private String powerfactor;
    private String deviceId;
    
    public PowerPhase() {
    }
    
    public String getActivepower() {
        return activepower;
    }

    public void setActivepower(String activepower) {
        this.activepower = activepower;
    }

    public String getApparentpower() {
        return apparentpower;
    }

    public void setApparentpower(String apparentpower) {
        this.apparentpower = apparentpower;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getPowerfactor() {
        return powerfactor;
    }

    public void setPowerfactor(String powerfactor) {
        this.powerfactor = powerfactor;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
}
