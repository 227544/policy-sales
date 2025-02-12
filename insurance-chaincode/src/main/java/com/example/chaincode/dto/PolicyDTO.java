package com.example.chaincode.dto;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.google.gson.Gson;
import java.util.List;

@DataType()
public class PolicyDTO {
    @Property()
    private String policyId;
    @Property()
    private String policyHolder;
    @Property()
    private double premium;
    @Property()
    private String policyStatus;
    @Property()
    private TravelInfoDTO travelInfo;
    @Property()
    private List<PassengerDTO> passengers;
    @Property()
    private InsuranceProductDTO insuranceProduct;

    public enum PolicyStatus {
        ACTIVE, INACTIVE, CANCELLED
    }

    public PolicyDTO() {
    }

    public PolicyDTO(String policyId, String policyHolder, double premium, String policyStatus,
            TravelInfoDTO travelInfo, List<PassengerDTO> passengers, InsuranceProductDTO insuranceProduct) {
        this.policyId = policyId;
        this.policyHolder = policyHolder;
        this.premium = premium;
        this.policyStatus = policyStatus;
        this.travelInfo = travelInfo;
        this.passengers = passengers;
        this.insuranceProduct = insuranceProduct;
    }

    // Getters e Setters

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

    public void setPolicyStatus(String newStatus) {
        this.policyStatus = newStatus;
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

    // Serialize to JSON
    public String toJson() {
        return new Gson().toJson(this);
    }

    // Deserialize from JSON
    public static PolicyDTO fromJson(String json) {
        return new Gson().fromJson(json, PolicyDTO.class);
    }
}
