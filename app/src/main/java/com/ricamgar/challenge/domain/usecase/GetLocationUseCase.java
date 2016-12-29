package com.ricamgar.challenge.domain.usecase;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import rx.Emitter;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class GetLocationUseCase implements GoogleApiClient.ConnectionCallbacks {

    private BehaviorSubject<Boolean> googleApiClientConnectedStream = BehaviorSubject.create();
    private GoogleApiClient googleApiClient;

    @Inject
    GetLocationUseCase(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        this.googleApiClient.registerConnectionCallbacks(this);
    }

    public Observable<LatLng> execute() {
        return googleApiClientConnectedStream
                .filter(isConnected -> isConnected)
                .flatMap(connected -> Observable.fromEmitter(latLngEmitter -> {

                    LocationListener locationListener = location -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        latLngEmitter.onNext(latLng);
                    };

                    latLngEmitter.setCancellation(() -> LocationServices.FusedLocationApi
                            .removeLocationUpdates(googleApiClient, locationListener));

                    //noinspection MissingPermission
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                            createLocationRequest(), locationListener);

                }, Emitter.BackpressureMode.DROP));
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        googleApiClientConnectedStream.onNext(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClientConnectedStream.onNext(false);
    }
}
