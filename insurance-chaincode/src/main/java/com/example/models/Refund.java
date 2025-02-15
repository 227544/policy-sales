package com.example.models;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Refund {
    private String policyId;
    private String policyHolder;
    private double refundAmount;
    private String policyStatus;

    private static final Gson gson = new Gson();

    public Refund() {
    }

    public Refund(String policyId, String policyHolder, double refundAmount, String policyStatus) {
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

    // Serialize to JSON
    public String toJson() {
        return gson.toJson(this);
    }

    // Deserialize from JSON
    public static Refund fromJson(String json) {
        try {
            return gson.fromJson(json, Refund.class);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Error converting JSON to Refund", e);
        }
    }
}
