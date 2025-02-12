package com.example.chaincode.dto;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class InsuranceProductDTO {
    @Property()
    private String coverageName;
    @Property()
    private double insuredAmount;

    public InsuranceProductDTO() {
    }

    public InsuranceProductDTO(String coverageName, double insuredAmount) {
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
        JSONObject json = new JSONObject();
        json.put("coverageName", coverageName);
        json.put("insuredAmount", insuredAmount);
        return json.toString();
    }

    // Deserialize from JSON
    public static InsuranceProductDTO fromJson(String json) {
        JSONObject jsonObj = new JSONObject(json);
        return new InsuranceProductDTO(
                jsonObj.getString("coverageName"),
                jsonObj.getDouble("insuredAmount"));
    }

}