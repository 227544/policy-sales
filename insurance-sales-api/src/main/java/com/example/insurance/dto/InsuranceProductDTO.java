package com.example.insurance.dto;

public class InsuranceProductDTO {
    private String coverageName;
    private double insuredAmount;

    public InsuranceProductDTO() {
    }

    public InsuranceProductDTO(String coverageName, double insuredAmount) {
        this.coverageName = coverageName;
        this.insuredAmount = insuredAmount;
    }

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
}
