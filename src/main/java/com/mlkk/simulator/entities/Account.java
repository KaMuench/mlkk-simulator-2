package com.mlkk.simulator.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class Account {
    private String name;
    private String surName;
    private String email;
    private String psw;
    private boolean superuser;

    private String token;

    private HashMap<Integer, Void> mFavorites;
    private HashMap<Integer, Void> mSignedUpForEvents;

    private int id;

    public Account(String name, String surname, String email, String pws, int id, boolean superuser) {
        this.name = name;
        this.surName = surname;
        this.email = email;
        this.psw = pws;
        this.id = id;
        this.superuser = superuser;
        mFavorites = new HashMap<>();
        mSignedUpForEvents = new HashMap<>();
    }

    @Override
    public String toString() {
        return String.format("%-10s%-10s%-30s%-20s%-20s%-5d", name, surName, email, psw, token, id);
    }

    public String toJson() {
        return "{\n" +
                "\t\"account_id\":" + id + ",\n" +
                "\t\"name\":\"" + name + "\",\n" +
                "\t\"surname\":\"" + surName + "\",\n" +
                "\t\"email\":\"" + email + "\",\n" +
                "\t\"superuser\":" + superuser + "\n" +
                "}";
    }


}
