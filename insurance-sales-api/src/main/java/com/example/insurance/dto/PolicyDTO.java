package com.example.insurance.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class PolicyDTO {
    private String policyId;
    private String policyHolder;
    private double premium;
    private String policyStatus;
    private TravelInfoDTO travelInfo;
    private List<PassengerDTO> passengers;
    private InsuranceProductDTO insuranceProduct;

    public PolicyDTO() {
    }

    public PolicyDTO(String policyId, String policyHolder, double premium, TravelInfoDTO travelInfo,
            List<PassengerDTO> passengers, InsuranceProductDTO insuranceProduct) {
        this.policyId = policyId;
        this.policyHolder = policyHolder;
        this.premium = premium;
        this.policyStatus = "ACTIVE";
        this.travelInfo = travelInfo;
        this.passengers = passengers;
        this.insuranceProduct = insuranceProduct;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public TravelInfoDTO getTravelInfo() {
        return travelInfo;
    }

    public void setTravelInfo(TravelInfoDTO travelInfo) {
        this.travelInfo = travelInfo;
    }

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public InsuranceProductDTO getInsuranceProduct() {
        return insuranceProduct;
    }

    public void setInsuranceProduct(InsuranceProductDTO insuranceProduct) {
        this.insuranceProduct = insuranceProduct;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting PolicyDTO to JSON", e);
        }
    }

    public static PolicyDTO fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, PolicyDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to PolicyDTO", e);
        }
    }
}