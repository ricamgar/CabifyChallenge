package com.ricamgar.challenge.presentation.main.presenter;

import com.ricamgar.challenge.domain.model.Location;
import com.ricamgar.challenge.domain.model.Stop;
import com.ricamgar.challenge.domain.usecase.EstimateJourneyUseCase;
import com.ricamgar.challenge.domain.usecase.ResolvePlaceUseCase;
import com.ricamgar.challenge.utils.EstimatesMother;
import com.ricamgar.challenge.utils.StopsMother;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private final Location ANY_LOCATION = new Location(1.0, 2.0);
    private final Stop ANY_STOP = new Stop(ANY_LOCATION, "any_name");

    private MainPresenter presenter;

    @Mock
    EstimateJourneyUseCase estimateJourney;
    @Mock
    ResolvePlaceUseCase resolvePlace;
    @Mock
    MainPresenter.MapView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new MainPresenter(estimateJourney, resolvePlace, Schedulers.immediate(),
                Schedulers.immediate());
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

    @Test
    public void shouldShowLoadingWhenBothOriginAndDestinationAreSelected() throws Exception {
        when(resolvePlace.execute("id")).thenReturn(Single.just(ANY_STOP));

        presenter.selectOriginId("id");

        verify(view, never()).showLoading();

        presenter.selectDestinationId("id");

        verify(view).showLoading();
    }

    @Test
    public void shouldEstimateJourneyWhenBothOriginAndDestinationAreSelected() throws Exception {
        when(resolvePlace.execute("id")).thenReturn(Single.just(ANY_STOP));
        when(estimateJourney.execute(StopsMother.ANY_STOPS_LIST))
                .thenReturn(Observable.just(EstimatesMother.ANY_ESTIMATE_LIST));

        presenter.selectOriginId("id");

        verify(estimateJourney, never()).execute(anyListOf(Stop.class));

        presenter.selectDestinationId("id");

        verify(estimateJourney).execute(anyListOf(Stop.class));
    }
}