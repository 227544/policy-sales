package com.example.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class TravelInfo {

    @Property()
    @SerializedName("destination")
    private String destination;

    @Property()
    @SerializedName("startDate")
    private String startDate;

    @Property()
    @SerializedName("endDate")
    private String endDate;

    @Property()
    @SerializedName("numberOfPassengers")
    private int numberOfPassengers;

    private static final Gson gson = new Gson();

    public TravelInfo() {
    }

    public TravelInfo(String destination, String startDate, String endDate, int numberOfPassengers) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPassengers = numberOfPassengers;
    }

    // Getters e Setters

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    // Serialize to JSON
    public String toJson() {
        return gson.toJson(this);
    }

    // Deserialize from JSON
    public static TravelInfo fromJson(String json) {
        return gson.fromJson(json, TravelInfo.class);
    }
}
