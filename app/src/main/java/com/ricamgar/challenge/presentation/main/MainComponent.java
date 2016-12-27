package com.ricamgar.challenge.presentation.main;


import com.google.android.gms.common.api.GoogleApiClient;
import com.ricamgar.challenge.domain.repository.EstimatesRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import rx.Scheduler;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);

    GoogleApiClient provideGoogleApiClient();

    EstimatesRepository provideEstimatesRepository();

    @Named(value = "mainThread")
    Scheduler provideMainScheduler();

    @Named(value = "ioThread")
    Scheduler provideIOScheduler();
}
