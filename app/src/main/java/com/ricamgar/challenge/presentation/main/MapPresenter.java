package com.ricamgar.challenge.presentation.main;


import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;

public class MapPresenter {

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private MapView view;
    private final GoogleApiClient googleApiClient;

    @Inject
    public MapPresenter(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void attachToView(MapView view) {
        this.view = view;
    }

    public void selectOriginId(String placeId) {
        subscriptions.add(resolvePlaceById(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        place -> view.addOriginMarker(place.getLatLng())
                ));
    }

    public void selectDestinationId(String placeId) {
        subscriptions.add(resolvePlaceById(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        place -> view.addDestinationMarker(place.getLatLng())
                ));
    }

    private Single<Place> resolvePlaceById(final String placeId) {
        return Single.fromCallable(() -> {
            PendingResult<PlaceBuffer> placeResult =
                    Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
            PlaceBuffer placeBuffer = placeResult.await(5, TimeUnit.SECONDS);
            if (!placeBuffer.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + placeBuffer.getStatus().toString());
                placeBuffer.release();
                throw new IllegalArgumentException("Place id not found");
            }
            return placeBuffer.get(0);
        });
    }

    public void detachFromView() {
        subscriptions.clear();
        view = null;
    }

    public interface MapView {

        void addOriginMarker(LatLng latLng);

        void addDestinationMarker(LatLng latLng);

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
