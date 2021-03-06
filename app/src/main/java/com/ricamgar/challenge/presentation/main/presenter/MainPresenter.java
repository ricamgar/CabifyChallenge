package com.ricamgar.challenge.presentation.main.presenter;

import android.Manifest;
import android.support.annotation.StringRes;

import com.google.android.gms.maps.GoogleMap;
import com.ricamgar.challenge.R;
import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.usecase.EstimateJourneyUseCase;
import com.ricamgar.challenge.domain.usecase.GetLocationUseCase;
import com.ricamgar.challenge.domain.usecase.ResolvePlaceUseCase;
import com.ricamgar.challenge.presentation.main.facade.MapFacade;
import com.tbruyelle.rxpermissions.RxPermissions;

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
    private final MapFacade mapFacade;
    private final RxPermissions rxPermissions;
    private final Scheduler mainThread;
    private final Scheduler ioThread;

    MainView view;

    @Inject
    public MainPresenter(EstimateJourneyUseCase estimateJourney, ResolvePlaceUseCase resolvePlace,
                         GetLocationUseCase getLocation, MapFacade mapFacade,
                         RxPermissions rxPermissions,
                         @Named("mainThread") Scheduler mainThread,
                         @Named("ioThread") Scheduler ioThread) {
        this.estimateJourney = estimateJourney;
        this.resolvePlace = resolvePlace;
        this.getLocation = getLocation;
        this.mapFacade = mapFacade;
        this.rxPermissions = rxPermissions;
        this.mainThread = mainThread;
        this.ioThread = ioThread;
    }

    public void attachToView(MainView view) {
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

    public void mapReady(GoogleMap googleMap) {
        mapFacade.init(googleMap);
    }

    public void selectOriginId(String placeId) {
        view.hideKeyboard();
        subscriptions.add(resolvePlace.execute(placeId)
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe(
                        origin -> {
                            mapFacade.addOriginMarker(origin.location);
                            originStream.onNext(origin);
                        },
                        throwable -> view.showError(R.string.error_resolve_place)
                ));
    }

    public void selectDestinationId(String placeId) {
        view.hideKeyboard();
        subscriptions.add(resolvePlace.execute(placeId)
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe(
                        destination -> {
                            mapFacade.addDestinationMarker(destination.location);
                            destinationStream.onNext(destination);
                        },
                        throwable -> view.showError(R.string.error_resolve_place)
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
                            this.view.showError(R.string.error_estimates);
                        }
                ));
    }

    private void subscribeToShowLoading(Observable<List<Stop>> originDestinationStream) {
        subscriptions.add(originDestinationStream
                .observeOn(mainThread)
                .subscribe(stops -> view.showLoading()));
    }

    private void subscribeToLocationUpdates() {
        subscriptions.add(rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .filter(granted -> granted)
                .first()
                .flatMap(granted -> getLocation.execute())
                .first()
                .map(latLng -> new Location(latLng.latitude, latLng.longitude))
                .subscribe(
                        location -> {
                            mapFacade.addOriginMarker(location);
                            originStream.onNext(new Stop(location, ""));
                            view.setOriginName(R.string.current_position);
                        },
                        throwable -> {
                            view.showError(R.string.no_location_permission);
                        }));
    }

    public interface MainView {

        void showEstimates(List<Estimate> estimates);

        void showLoading();

        void showError(@StringRes int errorMessage);

        void setOriginName(@StringRes int name);

        void hideKeyboard();
    }
}
