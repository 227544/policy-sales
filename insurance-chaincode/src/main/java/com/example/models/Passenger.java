package com.example.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class Passenger {

    @Property()
    @SerializedName("name")
    private String name;

    @Property()
    @SerializedName("document")
    private String document;

    @Property()
    @SerializedName("birthDate")
    private String birthDate;

    @Property()
    @SerializedName("email")
    private String email;

    @Property()
    @SerializedName("phone")
    private String phone;

    @Property()
    @SerializedName("address")
    private String address;

    private static final Gson gson = new Gson();

    public Passenger() {
    }

    public Passenger(String name, String document, String birthDate, String email, String phone, String address) {
        this.name = name;
        this.document = document;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.address = address;
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
    public String toJson() {
        return gson.toJson(this);
    }

    // Deserialize from JSON
    public static Passenger fromJson(String json) {
        return gson.fromJson(json, Passenger.class);
    }
}
