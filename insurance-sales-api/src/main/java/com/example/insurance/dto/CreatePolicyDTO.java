package com.example.insurance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePolicyDTO {

    @JsonProperty("policyHolder")
    private String policyHolder;

    @JsonProperty("documentId")
    private String documentId;

    @JsonProperty("premium")
    private double premium;

    @JsonProperty("travelInfo")
    private TravelInfoDTO travelInfo;

    @JsonProperty("passengers")
    private List<PassengerDTO> passengers;

    @JsonProperty("insuranceProduct")
    private InsuranceProductDTO insuranceProduct;

    public CreatePolicyDTO() {
    }

    public CreatePolicyDTO(String policyHolder, String documentId, double premium, TravelInfoDTO travelInfo,
            List<PassengerDTO> passengers, InsuranceProductDTO insuranceProduct) {
        this.policyHolder = policyHolder;
        this.documentId = documentId;
        this.premium = premium;
        this.travelInfo = travelInfo;
        this.passengers = passengers;
        this.insuranceProduct = insuranceProduct;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
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
            throw new RuntimeException("Error converting CreatePolicyDTO to JSON", e);
        }
    }

    public static CreatePolicyDTO fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, CreatePolicyDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to CreatePolicyDTO", e);
        }
    }
}