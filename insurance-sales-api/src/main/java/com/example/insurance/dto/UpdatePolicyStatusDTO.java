package com.example.insurance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePolicyStatusDTO {

    @JsonProperty("policyId")
    private String policyId;

    @JsonProperty("newStatus")
    private String newStatus;

    public UpdatePolicyStatusDTO() {
    }

    public UpdatePolicyStatusDTO(String policyId, String newStatus) {
        this.policyId = policyId;
        this.newStatus = newStatus;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting UpdatePolicyStatusDTO to JSON", e);
        }
    }

    public static UpdatePolicyStatusDTO fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, UpdatePolicyStatusDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to UpdatePolicyStatusDTO", e);
        }
    }
}