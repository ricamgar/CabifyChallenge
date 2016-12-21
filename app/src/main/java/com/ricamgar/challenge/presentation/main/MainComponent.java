package com.ricamgar.challenge.presentation.main;


import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);

    GoogleApiClient provideGoogleApiClient();
}
