package com.example.ecommerce.dto;

import jakarta.validation.constraints.Size;

public class ProfileUpdateRequest {

    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @Size(max = 100, message = "State must be at most 100 characters")
    private String state;

    @Size(max = 20, message = "Zip code must be at most 20 characters")
    private String zipCode;

    public ProfileUpdateRequest() {}

    public ProfileUpdateRequest(String firstName, String lastName, String phone, String address, String city, String state, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public static ProfileUpdateRequestBuilder builder() {
        return new ProfileUpdateRequestBuilder();
    }

    public static class ProfileUpdateRequestBuilder {
        private String firstName;
        private String lastName;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zipCode;

        public ProfileUpdateRequestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public ProfileUpdateRequestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public ProfileUpdateRequestBuilder phone(String phone) { this.phone = phone; return this; }
        public ProfileUpdateRequestBuilder address(String address) { this.address = address; return this; }
        public ProfileUpdateRequestBuilder city(String city) { this.city = city; return this; }
        public ProfileUpdateRequestBuilder state(String state) { this.state = state; return this; }
        public ProfileUpdateRequestBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }

        public ProfileUpdateRequest build() {
            return new ProfileUpdateRequest(firstName, lastName, phone, address, city, state, zipCode);
        }
    }
}
