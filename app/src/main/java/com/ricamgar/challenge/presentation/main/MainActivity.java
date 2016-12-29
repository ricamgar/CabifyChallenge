package com.ricamgar.challenge.presentation.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.ricamgar.challenge.R;
import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.presentation.main.adapter.EstimatesAdapter;
import com.ricamgar.challenge.presentation.main.adapter.PlaceAutocompleteAdapter;
import com.ricamgar.challenge.presentation.main.presenter.MainPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        MainPresenter.MainView {

    private static final String ORIGIN_ID = "ORIGIN_ID";
    private static final String DESTINATION_ID = "DESTINATION_ID";

    @BindView(R.id.autocomplete_origin_places)
    AutoCompleteTextView autoCompleteOrigin;
    @BindView(R.id.autocomplete_destination_places)
    AutoCompleteTextView autoCompleteDestination;
    @BindView(R.id.estimates_list)
    RecyclerView estimatesList;
    @BindView(R.id.loading)
    View loading;
    @BindView(R.id.estimates_container)
    View estimatesContainer;

    @Inject
    GoogleApiClient googleApiClient;
    @Inject
    MainPresenter presenter;
    @Inject
    EstimatesAdapter estimatesAdapter;

    private PlaceAutocompleteAdapter adapter;
    private String originId;
    private String destinationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainComponent.builder().mainModule(new MainModule(this)).build().inject(this);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new PlaceAutocompleteAdapter(this, googleApiClient);
        autoCompleteOrigin.setAdapter(adapter);
        autoCompleteOrigin.setOnItemClickListener((adapterView, view, i, l) -> {
            String originId = adapter.getItem(i).getPlaceId();
            this.originId = originId;
            presenter.selectOriginId(originId);
        });

        autoCompleteDestination.setAdapter(adapter);
        autoCompleteDestination.setOnItemClickListener((adapterView, view, i, l) -> {
            String destinationId = adapter.getItem(i).getPlaceId();
            this.destinationId = destinationId;
            presenter.selectDestinationId(destinationId);
        });

        estimatesList.setLayoutManager(new GridLayoutManager(this, 3));
        estimatesList.setAdapter(estimatesAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter.attachToView(this);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        originId = savedInstanceState.getString(ORIGIN_ID);
        destinationId = savedInstanceState.getString(DESTINATION_ID);
        if (!TextUtils.isEmpty(originId)) {
            presenter.selectOriginId(originId);
        }
        if (!TextUtils.isEmpty(destinationId)) {
            presenter.selectDestinationId(destinationId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ORIGIN_ID, originId);
        outState.putString(DESTINATION_ID, destinationId);
        super.onSaveInstanceState(outState);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        presenter.mapReady(googleMap);
    }

    @Override
    public void showError(int errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEstimates(List<Estimate> estimates) {
        estimatesContainer.setVisibility(View.VISIBLE);
        estimatesAdapter.addEstimates(estimates);
        estimatesList.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        autoCompleteDestination.clearFocus();
    }

    @Override
    public void showLoading() {
        estimatesContainer.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        estimatesList.setVisibility(View.GONE);
        autoCompleteDestination.clearFocus();
    }

    @Override
    public void setOriginName(int name) {
        autoCompleteOrigin.setHint(name);
        autoCompleteDestination.requestFocus();
    }

    @Override
    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
