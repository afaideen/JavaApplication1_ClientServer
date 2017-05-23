package javaapplication1.model;

import javaapplication1.MyEnergy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by han on 5/23/2017.
 */
public class EnergyInfo {
    private List<MyEnergy> listMonthlyEnergy = new ArrayList<>();

    public EnergyInfo() {
    }

    public void setlistMonthlyEnergy(List<MyEnergy> listMonthlyEnergy) {
        this.listMonthlyEnergy = listMonthlyEnergy;
    }
}
