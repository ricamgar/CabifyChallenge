package com.ricamgar.challenge.presentation.main.facade;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ricamgar.challenge.domain.model.Location;

import javax.inject.Inject;

public class MapFacade {

    private static final int MAP_PADDING = 140;
    private static final float CAMERA_ZOOM = 17.0f;

    private GoogleMap googleMap;
    private Marker originMarker;
    private Marker destinationMarker;
    private Polyline lineBetweenMarkers;

    @Inject
    public MapFacade() {
        // needed for DI
    }

    public void init(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setPadding(0, MAP_PADDING, 0, MAP_PADDING);
    }

    public void addOriginMarker(Location location) {
        removeMarkerIfNeeded(originMarker);
        originMarker = drawNewMarker(location);
        drawLineBetweenMarkers();
    }

    public void addDestinationMarker(Location location) {
        removeMarkerIfNeeded(destinationMarker);
        destinationMarker = drawNewMarker(location);
        drawLineBetweenMarkers();
    }

    private void removeMarkerIfNeeded(Marker marker) {
        if (marker != null) {
            marker.remove();
        }
    }

    private Marker drawNewMarker(Location location) {
        if (googleMap == null) return null;

        LatLng latLng = new LatLng(location.latitude, location.longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM));
        return googleMap.addMarker(new MarkerOptions().position(latLng));
    }

    private void drawLineBetweenMarkers() {
        if (googleMap == null) return;

        if (originMarker != null && destinationMarker != null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(originMarker.getPosition(), destinationMarker.getPosition());

            if (lineBetweenMarkers != null) {
                lineBetweenMarkers.remove();
            }

            lineBetweenMarkers = googleMap.addPolyline(polylineOptions);
            LatLngBounds latLngBounds = LatLngBounds.builder()
                    .include(originMarker.getPosition())
                    .include(destinationMarker.getPosition())
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    latLngBounds,
                    MAP_PADDING));
        }
    }
}
