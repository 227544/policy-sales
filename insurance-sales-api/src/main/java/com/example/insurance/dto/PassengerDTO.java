package com.example.insurance.dto;

public class PassengerDTO {
    private String name;
    private String document;
    private String birthDate;
    private String email;
    private String phone;
    private String address;

    public PassengerDTO() {
    }

    public PassengerDTO(String name, String document, String birthDate, String email, String phone, String address) {
        this.name = name;
        this.document = document;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}