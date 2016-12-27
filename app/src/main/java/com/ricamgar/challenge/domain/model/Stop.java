package com.ricamgar.challenge.domain.model;

import com.squareup.moshi.Json;

public class Stop {

    @Json(name = "loc")
    public final Location location;
    public final String name;

    public Stop(Location location, String name) {
        this.location = location;
        this.name = name;
    }
}
