package com.ricamgar.challenge.presentation.main.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.usecase.EstimateJourneyUseCase;
import com.ricamgar.challenge.domain.usecase.GetLocationUseCase;
import com.ricamgar.challenge.domain.usecase.ResolvePlaceUseCase;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;
import rx.subjects.ReplaySubject;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter {

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final ReplaySubject<Stop> originStream = ReplaySubject.create();
    private final ReplaySubject<Stop> destinationStream = ReplaySubject.create();

    private final EstimateJourneyUseCase estimateJourney;
    private final ResolvePlaceUseCase resolvePlace;
    private final GetLocationUseCase getLocation;
    private final Scheduler mainThread;
    private final Scheduler ioThread;

    MapView view;

    @Inject
    public MainPresenter(EstimateJourneyUseCase estimateJourney, ResolvePlaceUseCase resolvePlace,
                         GetLocationUseCase getLocation,
                         @Named("mainThread") Scheduler mainThread,
                         @Named("ioThread") Scheduler ioThread) {
        this.estimateJourney = estimateJourney;
        this.resolvePlace = resolvePlace;
        this.getLocation = getLocation;
        this.mainThread = mainThread;
        this.ioThread = ioThread;
    }

    public void attachToView(MapView view) {
        this.view = view;
        Observable<List<Stop>> originDestinationStream = createOriginAndDestinationStream();

        subscribeToShowLoading(originDestinationStream);
        subscribeToShowEstimates(originDestinationStream);
        subscribeToLocationUpdates();
    }

    public void detachFromView() {
        subscriptions.clear();
        view = null;
    }

    public void selectOriginId(String placeId) {
        subscriptions.add(resolvePlace.execute(placeId)
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe(
                        origin -> {
                            view.addOriginMarker(origin.location);
                            originStream.onNext(origin);
                        },
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    public void selectDestinationId(String placeId) {
        subscriptions.add(resolvePlace.execute(placeId)
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe(
                        destination -> {
                            view.addDestinationMarker(destination.location);
                            destinationStream.onNext(destination);
                        },
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    private Observable<List<Stop>> createOriginAndDestinationStream() {
        return Observable.combineLatest(
                originStream.observeOn(ioThread),
                destinationStream.observeOn(ioThread),
                (origin, destination) -> Arrays.asList(origin, destination));
    }

    private void subscribeToShowEstimates(Observable<List<Stop>> originDestinationStream) {
        subscriptions.add(originDestinationStream
                .flatMap(estimateJourney::execute)
                .observeOn(mainThread)
                .subscribe(
                        estimates -> {
                            this.view.showEstimates(estimates);
                        },
                        throwable -> {
                            this.view.showError(throwable.getMessage());
                        }
                ));
    }

    private void subscribeToShowLoading(Observable<List<Stop>> originDestinationStream) {
        subscriptions.add(originDestinationStream
                .observeOn(mainThread)
                .subscribe(stops -> view.showLoading()));
    }

    private void subscribeToLocationUpdates() {
        subscriptions.add(getLocation.execute()
                .first()
                .subscribe(
                        latLng -> view.showLocation(latLng)
                ));
    }

    public interface MapView {

        void addOriginMarker(Location latLng);

        void addDestinationMarker(Location latLng);

        void showError(String errorMessage);

        void showEstimates(List<Estimate> estimates);

        void showLoading();

        void showLocation(LatLng latLng);
    }
}
