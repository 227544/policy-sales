package com.example.insurance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelPolicyDTO {

    @JsonProperty("policyId")
    private String policyId;

    public CancelPolicyDTO() {
    }

    public CancelPolicyDTO(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting CancelPolicyDTO to JSON", e);
        }
    }

    public static CancelPolicyDTO fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, CancelPolicyDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to CancelPolicyDTO", e);
        }
    }
}