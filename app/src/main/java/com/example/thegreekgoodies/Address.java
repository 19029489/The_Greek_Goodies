package com.example.thegreekgoodies;

import java.io.Serializable;
import java.util.Optional;

public class Address implements Serializable {
    private String addressId;
    private String userId;
    private Optional<String>  firstname;
    private Optional<String>  lastname;
    private Optional<String> company;
    private String address1;
    private Optional<String> address2;
    private String city;
    private String country;
    private String postalCode;
    private Optional<String> phone;

    public Address(String addressId, String userId, Optional<String> firstname, Optional<String> lastname, Optional<String> company, String address1, Optional<String> address2, String city, String country, String postalCode, Optional<String> phone) {
        this.addressId = addressId;
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public String getAddressId() {
        return addressId;
    }
    public String getUserId() {
        return userId;
    }
    public Optional<String> getFirstname() {
        return firstname;
    }

    public void setFirstname(Optional<String> firstname) {
        this.firstname = firstname;
    }

    public Optional<String> getLastname() {
        return lastname;
    }

    public void setLastname(Optional<String> lastname) {
        this.lastname = lastname;
    }

    public Optional<String> getCompany() {
        return company;
    }

    public void setCompany(Optional<String> company) {
        this.company = company;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public Optional<String> getAddress2() {
        return address2;
    }

    public void setAddress2(Optional<String> address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Optional<String> getPhone() {
        return phone;
    }

    public void setPhone(Optional<String> phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname + "\n" +
                company + "\n" +
                address1 + "\n" +
                address2 + "\n" +
                city + " " + postalCode + "\n" +
                country;
    }
}
