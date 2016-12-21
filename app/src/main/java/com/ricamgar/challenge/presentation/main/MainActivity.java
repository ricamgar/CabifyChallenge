package com.ricamgar.challenge.presentation.main;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ricamgar.challenge.R;
import com.ricamgar.challenge.presentation.main.adapter.PlaceAutocompleteAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapPresenter.MapView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.autocomplete_origin_places)
    AutoCompleteTextView autoCompleteOrigin;
    @BindView(R.id.autocomplete_destination_places)
    AutoCompleteTextView autoCompleteDestination;

    @Inject
    GoogleApiClient googleApiClient;
    @Inject
    MapPresenter presenter;

    private GoogleMap map;
    private PlaceAutocompleteAdapter adapter;

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
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            }
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
    public void addOriginMarker(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
    }

    @Override
    public void addDestinationMarker(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
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
