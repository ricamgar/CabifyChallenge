package com.ricamgar.challenge.domain.usecase;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.ricamgar.challenge.data.mapper.PlaceToStopMapper;
import com.ricamgar.challenge.domain.model.Stop;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Single;

public class ResolvePlaceUseCase {

    private GoogleApiClient googleApiClient;
    private final PlaceToStopMapper placeToStopMapper;

    @Inject
    ResolvePlaceUseCase(GoogleApiClient googleApiClient, PlaceToStopMapper placeToStopMapper) {
        this.googleApiClient = googleApiClient;
        this.placeToStopMapper = placeToStopMapper;
    }

    public Single<Stop> execute(final String placeId) {
        return Single.fromCallable(() -> {
            PendingResult<PlaceBuffer> placeResult =
                    Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
            PlaceBuffer placeBuffer = placeResult.await(5, TimeUnit.SECONDS);
            if (!placeBuffer.getStatus().isSuccess()) {
                placeBuffer.release();
                throw new IllegalArgumentException("Place id not found");
            }
            Stop stop = placeToStopMapper.map(placeBuffer.get(0));
            placeBuffer.release();
            return stop;
        });
    }
}
