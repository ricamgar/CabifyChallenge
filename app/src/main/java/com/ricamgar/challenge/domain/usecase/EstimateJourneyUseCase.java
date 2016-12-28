package com.ricamgar.challenge.domain.usecase;


import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.repository.EstimatesRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class EstimateJourneyUseCase {

    private final EstimatesRepository estimatesRepository;

    @Inject
    EstimateJourneyUseCase(EstimatesRepository estimatesRepository) {
        this.estimatesRepository = estimatesRepository;
    }

    public Observable<List<Estimate>> execute(List<Stop> stops) {
        return estimatesRepository.estimateJourney(stops)
                            .onErrorReturn(throwable -> Collections.emptyList()).toObservable();
    }
}
