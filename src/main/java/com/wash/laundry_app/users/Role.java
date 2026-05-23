package com.wash.laundry_app.users;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    ADMIN,
    EMPLOYE,
    LIVREUR;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
