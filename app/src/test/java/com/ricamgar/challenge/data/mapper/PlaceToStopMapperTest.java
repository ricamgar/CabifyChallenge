package com.ricamgar.challenge.data.mapper;

import com.google.android.gms.location.places.Place;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.utils.PlaceMother;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaceToStopMapperTest {

    private PlaceToStopMapper placeToStopMapper;

    @Before
    public void setUp() throws Exception {
        placeToStopMapper = new PlaceToStopMapper();
    }

    @Test
    public void shouldMapPlaceToStop() throws Exception {
        Place place = PlaceMother.ANY_PLACE;
        Stop stop = placeToStopMapper.map(place);

        assertEquals(stop.location.latitude, place.getLatLng().latitude, 0.0);
        assertEquals(stop.location.longitude, place.getLatLng().longitude, 0.0);
        assertEquals(stop.name, place.getName());
    }
}