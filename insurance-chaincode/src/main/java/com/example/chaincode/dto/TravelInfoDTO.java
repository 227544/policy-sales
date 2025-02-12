package com.example.chaincode.dto;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class TravelInfoDTO {
    @Property()
    private String destination;
    @Property()
    private String startDate;
    @Property()
    private String endDate;
    @Property()
    private int numberOfPassengers;

    public TravelInfoDTO() {
    }

    public TravelInfoDTO(String destination, String startDate, String endDate, int numberOfPassengers) {
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
        JSONObject json = new JSONObject();
        json.put("destination", destination);
        json.put("startDate", startDate);
        json.put("endDate", endDate);
        json.put("numberOfPassengers", numberOfPassengers);
        return json.toString();
    }

    // Deserialize from JSON
    public static TravelInfoDTO fromJson(String json) {
        JSONObject jsonObj = new JSONObject(json);
        return new TravelInfoDTO(
                jsonObj.getString("destination"),
                jsonObj.getString("startDate"),
                jsonObj.getString("endDate"),
                jsonObj.getInt("numberOfPassengers"));
    }
}