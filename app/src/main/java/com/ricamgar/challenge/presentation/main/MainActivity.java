package com.ricamgar.challenge.presentation.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ricamgar.challenge.R;
import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.presentation.main.adapter.PlaceAutocompleteAdapter;
import com.ricamgar.challenge.presentation.main.presenter.MainPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        MainPresenter.MapView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.autocomplete_origin_places)
    AutoCompleteTextView autoCompleteOrigin;
    @BindView(R.id.autocomplete_destination_places)
    AutoCompleteTextView autoCompleteDestination;

    @Inject
    GoogleApiClient googleApiClient;
    @Inject
    MainPresenter presenter;

    private GoogleMap map;
    private PlaceAutocompleteAdapter adapter;
    private Marker originMarker;
    private Marker destinationMarker;
    private Polyline lineBetweenMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainComponent.builder().mainModule(new MainModule(this)).build().inject(this);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new PlaceAutocompleteAdapter(this, googleApiClient, null, null);
        autoCompleteOrigin.setAdapter(adapter);
        autoCompleteOrigin.setOnItemClickListener((adapterView, view, i, l) ->
                presenter.selectOriginId(adapter.getItem(i).getPlaceId()));

        autoCompleteDestination.setAdapter(adapter);
        autoCompleteDestination.setOnItemClickListener((adapterView, view, i, l) ->
                presenter.selectDestinationId(adapter.getItem(i).getPlaceId()));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter.attachToView(this);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.detachFromView();
        super.onDestroy();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        // TODO: 21/12/16 Use the FusedLocationApi
        map.setOnMyLocationChangeListener(location -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        });
    }

//    @SuppressWarnings("MissingPermission")
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
////        requestLocationUpdates();
//    }

//    @SuppressWarnings("MissingPermission")
//    private void requestLocationUpdates() {
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        // TODO: 21/12/16
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // TODO: 21/12/16
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        if (lastLocation == null) {
//            lastLocation = location;
//            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            map.addMarker(new MarkerOptions().position(latLng));
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
//            autoCompleteOrigin.setHint("My current location");
//        }
//    }

    @Override
    public void addOriginMarker(Location location) {
        removeMarkerIfNeeded(originMarker);
        originMarker = drawNewMarker(location);
        drawLineBetweenMarkers();
    }

    @Override
    public void addDestinationMarker(Location location) {
        removeMarkerIfNeeded(destinationMarker);
        destinationMarker = drawNewMarker(location);
        drawLineBetweenMarkers();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }

    @Override
    public void showEstimates(List<Estimate> estimates) {
        Toast.makeText(this, "Estimates: " + estimates.size(), Toast.LENGTH_SHORT).show();
    }

    private void removeMarkerIfNeeded(Marker marker) {
        if (marker != null) {
            marker.remove();
        }
    }

    private Marker drawNewMarker(Location location) {
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        return map.addMarker(new MarkerOptions().position(latLng));
    }

    private void drawLineBetweenMarkers() {
        if (originMarker != null && destinationMarker != null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(originMarker.getPosition(), destinationMarker.getPosition());
            if (lineBetweenMarkers != null) {
                lineBetweenMarkers.remove();
            }
            lineBetweenMarkers = map.addPolyline(polylineOptions);
            LatLngBounds latLngBounds = LatLngBounds.builder()
                    .include(originMarker.getPosition())
                    .include(destinationMarker.getPosition())
                    .build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    latLngBounds,
                    20));
        }
    }

//    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
//                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
//        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
//                websiteUri));
//        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
//                websiteUri));
//
//    }
}
