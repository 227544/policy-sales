package com.example.chaincode.dto;

import com.google.gson.Gson;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DataType()
public class PassengerDTO {

    @Property()
    private String name;
    @Property()
    private String document;
    @Property()
    private String birthDate;
    @Property()
    private String email;
    @Property()
    private String phone;
    @Property()
    private String address;
    private static final Gson objectMapper = new Gson();

    // Constructor with all parameters
    public PassengerDTO(String name, String document, String birthDate, String email, String phone, String address) {
        this.name = name;
        this.document = document;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public PassengerDTO(Builder builder) {
        this.name = builder.name;
        this.document = builder.document;
        this.birthDate = builder.birthDate;
        this.email = builder.email;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    public static class Builder {
        private String name;
        private String document;
        private String birthDate;
        private String email;
        private String phone;
        private String address;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDocument(String document) {
            this.document = document;
            return this;
        }

        public Builder setBirthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public PassengerDTO build() {
            return new PassengerDTO(this);
        }
    }

    // Getters e Setters

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

    // Serialize to JSON
    public String toJson() throws IOException {
        return objectMapper.toJson(this);
    }

    // Deserialize from JSON
    public static PassengerDTO fromJson(String json) throws IOException {
        return objectMapper.fromJson(json, PassengerDTO.class);
    }

    // Deserialize from JSON Array
    public static List<PassengerDTO> fromJsonArray(String jsonArray) throws IOException {
        JSONArray array = new JSONArray(jsonArray);
        List<PassengerDTO> passengers = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            PassengerDTO passenger = PassengerDTO.fromJson(jsonObject.toString());
            passengers.add(passenger);
        }
        return passengers;
    }
}