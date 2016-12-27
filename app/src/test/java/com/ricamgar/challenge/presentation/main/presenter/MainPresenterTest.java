package com.ricamgar.challenge.presentation.main.presenter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.ricamgar.challenge.data.mapper.PlaceToStopMapper;
import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.usecase.EstimateJourneyUseCase;
import com.ricamgar.challenge.domain.usecase.ResolvePlaceUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private final Location ANY_LOCATION = new Location(1.0, 2.0);
    private final Stop ANY_STOP = new Stop(ANY_LOCATION, "any_name");

    MainPresenter presenter;

    @Mock
    GoogleApiClient googleApiClient;
    @Mock
    EstimateJourneyUseCase estimateJourney;
    @Mock
    ResolvePlaceUseCase resolvePlace;
    @Mock
    MainPresenter.MapView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(estimateJourney.bind()).thenReturn(Observable.empty());

        presenter = new MainPresenter(googleApiClient, estimateJourney, resolvePlace,
                new PlaceToStopMapper(), Schedulers.immediate(), Schedulers.immediate());
        presenter.attachToView(view);
    }

    @Test
    public void shouldInitializeViewOnAttachToView() throws Exception {
        assertNotNull(presenter.view);
    }

    @Test
    public void shouldSetViewToNullOnDetach() throws Exception {
        presenter.detachFromView();

        assertNull(presenter.view);
    }

    @Test
    public void shouldBindWithEstimateJourneyOnInit() throws Exception {
        verify(estimateJourney).bind();
    }

    @Test
    public void shouldAddMarkerWhenOriginIsSelected() throws Exception {
        when(resolvePlace.execute("id")).thenReturn(Single.just(ANY_STOP));

        presenter.selectOriginId("id");

        verify(view).addOriginMarker(ANY_LOCATION);
    }

    @Test
    public void shouldAddMarkerWhenDestinationIsSelected() throws Exception {
        when(resolvePlace.execute("id")).thenReturn(Single.just(ANY_STOP));

        presenter.selectDestinationId("id");

        verify(view).addDestinationMarker(ANY_LOCATION);
    }
}