package com.example.insurance.dto;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RefundDTO {
    private String policyId;
    private String policyHolder;
    private double refundAmount;
    private String policyStatus;

    public RefundDTO() {
    }

    public RefundDTO(String refundId, String policyId, String policyHolder, double refundAmount, String policyStatus) {
        this.policyId = policyId;
        this.policyHolder = policyHolder;
        this.refundAmount = refundAmount;
        this.policyStatus = policyStatus;
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

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static RefundDTO fromJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, RefundDTO.class);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Error converting JSON to RefundDTO", e);
        }
    }
}