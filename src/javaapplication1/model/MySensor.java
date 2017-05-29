package javaapplication1.model;

import javaapplication1.MyEnergy;

/**
 * Created by han on 5/29/2017.
 */
public class MySensor {
    private MyEnergy energy;
    private String id;

    public MySensor() {
    }

    public void setEnergy(MyEnergy energy) {
        this.energy = energy;
    }

    public MyEnergy getEnergy() {
        return energy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
