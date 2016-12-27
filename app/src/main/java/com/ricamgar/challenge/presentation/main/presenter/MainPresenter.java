package com.ricamgar.challenge.presentation.main.presenter;


import com.google.android.gms.common.api.GoogleApiClient;
import com.ricamgar.challenge.data.mapper.PlaceToStopMapper;
import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.usecase.EstimateJourneyUseCase;
import com.ricamgar.challenge.domain.usecase.ResolvePlaceUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Scheduler;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter {

    final CompositeSubscription subscriptions = new CompositeSubscription();
    MapView view;

    private final GoogleApiClient googleApiClient;
    private final EstimateJourneyUseCase estimateJourney;
    private final ResolvePlaceUseCase resolvePlace;
    private final PlaceToStopMapper placeToStopMapper;
    private final Scheduler mainThread;
    private final Scheduler ioThread;

    @Inject
    public MainPresenter(GoogleApiClient googleApiClient, EstimateJourneyUseCase estimateJourney,
                         ResolvePlaceUseCase resolvePlace,
                         PlaceToStopMapper placeToStopMapper,
                         @Named("mainThread") Scheduler mainThread,
                         @Named("ioThread") Scheduler ioThread) {
        this.googleApiClient = googleApiClient;
        this.estimateJourney = estimateJourney;
        this.resolvePlace = resolvePlace;
        this.placeToStopMapper = placeToStopMapper;
        this.mainThread = mainThread;
        this.ioThread = ioThread;
    }

    public void attachToView(MapView view) {
        this.view = view;
        subscriptions.add(estimateJourney.bind()
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

    public void selectOriginId(String placeId) {
        subscriptions.add(resolvePlace.execute(placeId)
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe(
                        origin -> {
                            view.addOriginMarker(origin.location);
                            estimateJourney.addOrigin(origin);
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
                            estimateJourney.addDestination(destination);
                        },
                        throwable -> view.showError(throwable.getMessage())
                ));
    }

    public void detachFromView() {
        subscriptions.clear();
        view = null;
    }

    public interface MapView {

        void addOriginMarker(Location latLng);

        void addDestinationMarker(Location latLng);

        void showError(String errorMessage);

        void showEstimates(List<Estimate> estimates);
    }

//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
//            = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(PlaceBuffer places) {
//            if (!places.getStatus().isSuccess()) {
//                // Request did not complete successfully
//                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
//                places.release();
//                return;
//            }
//            // Get the Place object from the buffer.
//            final Place place = places.get(0);
////            map.addMarker(new MarkerOptions().position(place.getLatLng()));
////            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17.0f));
////            // Format details of the place for display and show it in a TextView.
////            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
////                    place.getId(), place.getAddress(), place.getPhoneNumber(),
////                    place.getWebsiteUri()));
////
////            // Display the third party attributions if set.
////            final CharSequence thirdPartyAttribution = places.getAttributions();
////            if (thirdPartyAttribution == null) {
////                mPlaceDetailsAttribution.setVisibility(View.GONE);
////            } else {
////                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
////                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
////            }
//
//            Log.i(TAG, "Place details received: " + place.getName());
//
//            places.release();
//        }
//    };
}
