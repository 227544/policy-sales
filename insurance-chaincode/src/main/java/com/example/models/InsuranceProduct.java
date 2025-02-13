package com.example.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class InsuranceProduct {

    @Property()
    @SerializedName("coverageName")
    private String coverageName;

    @Property()
    @SerializedName("insuredAmount")
    private double insuredAmount;

    private static final Gson gson = new Gson();

    public InsuranceProduct() {
    }

    public InsuranceProduct(String coverageName, double insuredAmount) {
        this.coverageName = coverageName;
        this.insuredAmount = insuredAmount;
    }

    // Getters e Setters

    public String getCoverageName() {
        return coverageName;
    }

    public void setCoverageName(String coverageName) {
        this.coverageName = coverageName;
    }

    public double getInsuredAmount() {
        return insuredAmount;
    }

    public void setInsuredAmount(double insuredAmount) {
        this.insuredAmount = insuredAmount;
    }

    // Serialize to JSON
    public String toJson() {
        return gson.toJson(this);
    }

    // Deserialize from JSON
    public static InsuranceProduct fromJson(String json) {
        return gson.fromJson(json, InsuranceProduct.class);
    }
}