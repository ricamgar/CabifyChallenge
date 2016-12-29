package com.ricamgar.challenge.data.repository.adapter;

import com.ricamgar.challenge.domain.model.Location;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

@SuppressWarnings("unused")
public class LocationAdapter {

    @ToJson
    double[] toJson(Location location) {
        return new double[]{location.latitude, location.longitude};
    }

    @FromJson
    Location fromJson(double[] location) {
        return new Location(location[0], location[1]);
    }
}
