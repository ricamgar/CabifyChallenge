package com.ricamgar.challenge.domain.model;


import com.squareup.moshi.Json;

import java.util.List;

public class EstimateRequest {

    public final List<Stop> stops;
    @Json(name = "start_at")
    public final String startAt;

    public EstimateRequest(List<Stop> stops, String startAt) {
        this.stops = stops;
        this.startAt = startAt;
    }
}
