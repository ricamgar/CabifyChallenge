package com.ricamgar.challenge.domain.model;

import com.squareup.moshi.Json;

public class VehicleType {

    @Json(name = "short_name")
    public final String shortName;
    public final Icons icons;

    public VehicleType(String shortName, Icons icons) {
        this.shortName = shortName;
        this.icons = icons;
    }
}
