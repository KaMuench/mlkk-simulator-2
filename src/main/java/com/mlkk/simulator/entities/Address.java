package com.mlkk.simulator.entities;

import jdk.jfr.SettingDefinition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Address implements Serializable {
    private String street;
    private String hNumber;
    private String posCode;
    private String city;

    private static int id;

    public Address() {
        id++;
    }

    public Address(String street, String hNumber, String posCode, String city) {
        this.street = street;
        this.hNumber = hNumber;
        this.posCode = posCode;
        this.city = city;
        id++;
    }

    public String toJson() {
        return "{\n" +
                "\t\t\t\t\"address_id\": " + id + ",\n" +
                "\t\t\t\t\"street\": \"" + street + "\",\n" +
                "\t\t\t\t\"house_number\": " + hNumber + ",\n" +
                "\t\t\t\t\"post_code\": " + posCode + ",\n" +
                "\t\t\t\t\"city\": \"" + city + "\"\n" +
                "\t\t\t}";
    }
}
