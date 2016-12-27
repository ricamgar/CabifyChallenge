package com.ricamgar.challenge.data.mapper;


import com.google.android.gms.location.places.Place;
import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.model.Stop;

import javax.inject.Inject;

public class PlaceToStopMapper {

    @Inject
    public PlaceToStopMapper() {
        // needed for DI
    }

    public Stop map(Place place) {
        Location location = new Location(place.getLatLng().latitude, place.getLatLng().longitude);
        return new Stop(location, place.getName().toString());
    }
}
