package com.example.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Policy {

    @SerializedName("policyId")
    private String policyId;

    @SerializedName("policyHolder")
    private String policyHolder;

    @SerializedName("documentId")
    private String documentId;

    @SerializedName("premium")
    private double premium;

    @SerializedName("policyStatus")
    private String policyStatus;

    @SerializedName("travelInfo")
    private TravelInfo travelInfo;

    @SerializedName("passengers")
    private List<Passenger> passengers;

    @SerializedName("insuranceProduct")
    private InsuranceProduct insuranceProduct;

    public enum PolicyStatus {
        ACTIVE, CANCELLED, CLOSED
    }

    private PolicyStatus policyStatusEnum;

    private static final Gson gson = new Gson();

    public Policy() {
    }

    public Policy(String policyId, String policyHolder, String documentId, double premium, String policyStatus,
            TravelInfo travelInfo, List<Passenger> passengers, InsuranceProduct insuranceProduct) {
        this.policyId = policyId;
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

    public TravelInfo getTravelInfo() {
        return travelInfo;
    }

    public void setTravelInfo(TravelInfo travelInfo) {
        this.travelInfo = travelInfo;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct;
    }

    public void setInsuranceProduct(InsuranceProduct insuranceProduct) {
        this.insuranceProduct = insuranceProduct;
    }

    public PolicyStatus getPolicyStatusEnum() {
        return policyStatusEnum;
    }

    public void setPolicyStatusEnum(String status) {
        this.policyStatusEnum = PolicyStatus.valueOf(status);
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static Policy fromJson(String json) {
        return gson.fromJson(json, Policy.class);
    }
}
