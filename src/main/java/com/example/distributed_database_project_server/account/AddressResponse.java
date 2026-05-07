package com.example.distributed_database_project_server.account;

import java.util.UUID;

import com.example.distributed_database_project_server.domain.entity.AddressEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {

    private UUID accountId;
    private UUID addressId;
    private String street;
    private String city;
    private String region;
    private String country;
    private String postalCode;

    private AddressResponse(UUID accountId, UUID addressId, String street, String city, String region, String country, String postalCode) {
        this.accountId = accountId;
        this.addressId = addressId;
        this.street = street;
        this.city = city;
        this.region = region;
        this.country = country;
        this.postalCode = postalCode;
    }

    public static AddressResponse fromEntity(AddressEntity address) {
        return new AddressResponse(
                address.getCustomer().getAccount().getId(),
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getRegion(),
                address.getCountry(),
                address.getPostalCode()
        );
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
}
