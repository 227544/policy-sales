package com.example.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class Policy {
    @Property()
    private String policyId;
    @Property()
    private String holderName;
    @Property()
    private double premium;
    @Property()
    private String status;

    // Construtor padrão (necessário para desserialização)
    public Policy() {
    }

    public Policy(String policyId, String holderName, double premium, String status) {
        this.policyId = policyId;
        this.holderName = holderName;
        this.premium = premium;
        this.status = status;
    }

    // Getters e Setters
    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Serialize to JSON
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("policyId", policyId);
        json.put("holderName", holderName);
        json.put("premium", premium);
        json.put("status", status);
        return json.toString();
    }

    // Deserialize from JSON
    public static Policy fromJson(String json) {
        JSONObject jsonObj = new JSONObject(json);
        return new Policy(
                jsonObj.getString("policyId"),
                jsonObj.getString("holderName"),
                jsonObj.getDouble("premium"),
                jsonObj.getString("status"));
    }
}
