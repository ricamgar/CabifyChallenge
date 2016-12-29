package com.ricamgar.challenge.presentation.main;


import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.ricamgar.challenge.data.api.CabifyApi;
import com.ricamgar.challenge.data.repository.EstimatesRemoteRepository;
import com.ricamgar.challenge.data.repository.adapter.LocationAdapter;
import com.ricamgar.challenge.domain.repository.EstimatesRepository;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class MainModule {

    private final Activity activity;

    public MainModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(activity)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .build();
    }

    @Provides
    @Singleton
    EstimatesRepository provideEstimatesRepository() {
        Moshi moshi = new Moshi.Builder()
                .add(new LocationAdapter())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl("https://test.cabify.com")
                .build();

        return new EstimatesRemoteRepository(retrofit.create(CabifyApi.class));
    }

    @Singleton
    @Provides
    @Named(value = "mainThread")
    Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Singleton
    @Provides
    @Named(value = "ioThread")
    Scheduler provideIOScheduler() {
        return Schedulers.io();
    }

    @Singleton
    @Provides
    Picasso providePicasso() {
        return Picasso.with(activity);
    }

    @Singleton
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(activity);
    }
}
