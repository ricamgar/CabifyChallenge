package com.ricamgar.challenge.domain.model;

import com.squareup.moshi.Json;

public class Estimate {

    @Json(name = "vehicle_type")
    public final VehicleType vehicleType;
    @Json(name = "price_formatted")
    public final String priceFormatted;

    public Estimate(VehicleType vehicleType, String priceFormatted) {
        this.vehicleType = vehicleType;
        this.priceFormatted = priceFormatted;
    }
}
