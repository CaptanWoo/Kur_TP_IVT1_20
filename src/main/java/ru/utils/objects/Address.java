package ru.utils.objects;

import ru.db.DataBaseTable;
import lombok.Data;

@Data
public class Address {

    private String country;
    private String region;
    private String city;
    private String street;
    private String house;

    public Address() {
        this.country = "";
        this.region = "";
        this.city = "";
        this.street = "";
        this.house = "";
    }

    public Address(String data) {
        load(data);
    }

    public Address(String country, String region, String city, String street, String house) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public void load(String text) {
        String[] data = text.split(DataBaseTable.textArraySeparator);
        if (data.length != 5) {
            String[] temp = new String[5];
            System.arraycopy(data, 0, temp, 0, data.length);
            for (int i = 0; i < temp.length; i++) if (temp[i] == null) temp[i] = "";
            data = temp;
        }
        country = data[0];
        region = data[1];
        city = data[2];
        street = data[3];
        house = data[4];
    }

    public String save() {
        return country + DataBaseTable.textArraySeparator +
                region + DataBaseTable.textArraySeparator +
                city + DataBaseTable.textArraySeparator +
                street + DataBaseTable.textArraySeparator +
                house;
    }

    public String getDisplayAddress() {
        StringBuilder sb = new StringBuilder();
        if (!country.equals("")) sb.append(", ").append(country);
        if (!region.equals("")) sb.append(", ").append(region);
        if (!city.equals("")) sb.append(", ").append(city);
        if (!street.equals("")) sb.append(", ").append(street);
        if (!house.equals("")) sb.append(", ").append(house);

        if (sb.length() > 2) return sb.substring(2);
        else return sb.toString();
    }

    public void clear() {
        country = "";
        region = "";
        city = "";
        street = "";
        house = "";
    }

}
