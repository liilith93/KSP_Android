package com.example.ula.ksp_projekt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ula on 22.05.2017.
 */

public class Measurment {

    @SerializedName("temperature")
    int temperature;

    @SerializedName("humidity")
    int humidity;

    @SerializedName("lastUpdate")
    String lastUpdate;

    public Measurment(int temperature, int humidity, String lastUpdate){
        this.temperature = temperature;
        this.humidity = humidity;
        this.lastUpdate = lastUpdate;
    }
}
