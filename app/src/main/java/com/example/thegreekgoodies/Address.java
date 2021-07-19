package com.example.thegreekgoodies;

import java.io.Serializable;

public class Address implements Serializable {
    private String addressId;
    private String userId;
    private String firstname;
    private String lastname;
    private String company;
    private String address1;
    private String address2;
    private String city;
    private String country;
    private String postalCode;
    private String phone;
    private Boolean defaultAddress;

    public Address(String addressId, String userId, String firstname, String lastname, String company, String address1, String address2, String city, String country, String postalCode, String phone, Boolean defaultAddress) {
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
        this.defaultAddress = defaultAddress;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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
