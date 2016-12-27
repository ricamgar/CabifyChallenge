package com.ricamgar.challenge.domain.repository;


import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Stop;

import java.util.List;

import rx.Single;


public interface EstimatesRepository {

    Single<List<Estimate>> estimateJourney(List<Stop> stops);
}
