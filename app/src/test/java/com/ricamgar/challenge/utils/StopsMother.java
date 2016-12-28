package com.ricamgar.challenge.utils;


import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.model.Stop;

import java.util.Arrays;
import java.util.List;

public class StopsMother {

    public static final Location ANY_STOP_LOCATION = new Location(1.0, 2.0);
    public static final String ANY_STOP_NAME = "ANY_STOP_NAME";
    public static final Stop ANY_STOP = new Stop(ANY_STOP_LOCATION, type, ANY_STOP_NAME);
    public static final List<Stop> ANY_STOPS_LIST = Arrays.asList(ANY_STOP, ANY_STOP);
}
