package com.ricamgar.challenge.data.repository;


import com.ricamgar.challenge.data.api.CabifyApi;
import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.EstimateRequest;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.repository.EstimatesRepository;

import java.util.List;

import rx.Single;

public class EstimatesRemoteRepository implements EstimatesRepository {

    private final CabifyApi cabifyApi;

    public EstimatesRemoteRepository(CabifyApi cabifyApi) {
        this.cabifyApi = cabifyApi;
    }

    @Override
    public Single<List<Estimate>> estimateJourney(List<Stop> stops) {
        return cabifyApi.getEstimates(new EstimateRequest(stops, ""));
    }
}
