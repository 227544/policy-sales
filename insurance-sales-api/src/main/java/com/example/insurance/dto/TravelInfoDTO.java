package com.example.insurance.dto;

import java.util.Date;

public class TravelInfoDTO {
    private String destination;
    private Date startDate;
    private Date endDate;
    private int numberOfPassengers;

    public TravelInfoDTO() {
    }

    public TravelInfoDTO(String destination, Date startDate, Date endDate, int numberOfPassengers) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }
}