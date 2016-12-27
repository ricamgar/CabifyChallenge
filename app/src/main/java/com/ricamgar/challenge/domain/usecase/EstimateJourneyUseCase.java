package com.ricamgar.challenge.domain.usecase;


import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.repository.EstimatesRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;
import rx.subjects.PublishSubject;

public class EstimateJourneyUseCase {

    private PublishSubject<Stop> originStream = PublishSubject.create();
    private PublishSubject<Stop> destinationStream = PublishSubject.create();
    private final EstimatesRepository estimatesRepository;
    private final Scheduler ioThread;

    @Inject
    EstimateJourneyUseCase(EstimatesRepository estimatesRepository,
                           @Named("ioThread") Scheduler ioThread) {
        this.estimatesRepository = estimatesRepository;
        this.ioThread = ioThread;
    }

    public Observable<List<Estimate>> bind() {
        return Observable.combineLatest(
                originStream.observeOn(ioThread),
                destinationStream.observeOn(ioThread),
                (origin, destination) -> Arrays.asList(origin, destination))
                .flatMap(stops -> {
                    return estimatesRepository.estimateJourney(stops)
                            .onErrorReturn(throwable -> Collections.emptyList()).toObservable();
                });
    }

    public void addOrigin(Stop origin) {
        originStream.onNext(origin);
    }

    public void addDestination(Stop destination) {
        destinationStream.onNext(destination);
    }
}
