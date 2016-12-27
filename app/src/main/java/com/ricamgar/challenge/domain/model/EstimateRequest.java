package com.ricamgar.challenge.domain.model;


import java.util.List;

public class EstimateRequest {

    public final List<Stop> stops;
    public final String startAt;

    public EstimateRequest(List<Stop> stops, String startAt) {
        this.stops = stops;
        this.startAt = startAt;
    }
}
