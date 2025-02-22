package com.example.insurance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDTO {

    @JsonProperty("policyId")
    private String policyId;

    @JsonProperty("policyHolder")
    private String policyHolder;

    @JsonProperty("documentId")
    private String documentId;

    @JsonProperty("premium")
    private double premium;

    @JsonProperty("policyStatus")
    private String policyStatus;

    @JsonProperty("travelInfo")
    private TravelInfoDTO travelInfo;

    @JsonProperty("passengers")
    private List<PassengerDTO> passengers;

    @JsonProperty("insuranceProduct")
    private InsuranceProductDTO insuranceProduct;

    public PolicyDTO() {
    }

    public PolicyDTO(String policyHolder, String documentId, double premium, TravelInfoDTO travelInfo,
            List<PassengerDTO> passengers, InsuranceProductDTO insuranceProduct, String policyStatus) {
        this.policyHolder = policyHolder;
        this.documentId = documentId;
        this.premium = premium;
        this.policyStatus = policyStatus;
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

    public static List<PolicyDTO> fromJsonList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<PolicyDTO>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to List<PolicyDTO>", e);
        }
    }
}