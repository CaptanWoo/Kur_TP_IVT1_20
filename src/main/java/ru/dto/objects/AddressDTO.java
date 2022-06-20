package ru.dto.objects;

import lombok.Getter;

@Getter
public class AddressDTO {
    private final String country, region, city, street, house;

    public AddressDTO(String country, String region, String city, String street, String house) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
    }
}
