package com.ricamgar.challenge.utils;


import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Icons;
import com.ricamgar.challenge.domain.model.VehicleType;

import java.util.Arrays;
import java.util.List;

public class EstimatesMother {

    public static final String ANY_NAME = "ANY_NAME";
    public static final String ANY_ICON = "ANY_ICON";
    public static final VehicleType ANY_VEHICLE_TYPE = new VehicleType(ANY_NAME, new Icons(ANY_ICON));
    public static final String ANY_PRICE = "ANY_PRICE";
    public static final Estimate ANY_ESTIMATE = new Estimate(ANY_VEHICLE_TYPE, ANY_PRICE);
    public static final List<Estimate> ANY_ESTIMATE_LIST =
            Arrays.asList(ANY_ESTIMATE, ANY_ESTIMATE);
}
