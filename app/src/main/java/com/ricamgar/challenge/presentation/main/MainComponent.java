package com.ricamgar.challenge.presentation.main;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
interface MainComponent {

    void inject(MainActivity mainActivity);
}
