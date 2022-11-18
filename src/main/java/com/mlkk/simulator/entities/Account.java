package com.mlkk.simulator.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Account {
    private String name;
    private String surName;
    private String email;
    private String psw;

    @Override
    public String toString() {
        return String.format("%-10s%-10s%-30s%-20s", name, surName, email, psw);
    }
}
