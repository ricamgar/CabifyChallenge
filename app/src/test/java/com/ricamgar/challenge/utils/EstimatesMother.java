package com.ricamgar.challenge.utils;


import com.ricamgar.challenge.domain.model.Estimate;

import java.util.Arrays;
import java.util.List;

public class EstimatesMother {

    public static final Estimate ANY_ESTIMATE = new Estimate();
    public static final List<Estimate> ANY_ESTIMATE_LIST =
            Arrays.asList(ANY_ESTIMATE, ANY_ESTIMATE);
}
