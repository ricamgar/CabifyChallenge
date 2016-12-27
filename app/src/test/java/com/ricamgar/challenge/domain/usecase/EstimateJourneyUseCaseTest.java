package com.ricamgar.challenge.domain.usecase;

import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.repository.EstimatesRepository;
import com.ricamgar.challenge.utils.EstimatesMother;
import com.ricamgar.challenge.utils.StopsMother;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Single;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.when;

public class EstimateJourneyUseCaseTest {

    private EstimateJourneyUseCase estimateJourney;

    @Mock
    EstimatesRepository estimatesRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        estimateJourney = new EstimateJourneyUseCase(estimatesRepository, Schedulers.immediate());
    }

    @Test
    public void shouldNotExecuteWhenJustOriginIsSelected() throws Exception {
        TestSubscriber<List<Estimate>> testSubscriber = new TestSubscriber<>();
        estimateJourney.bind().subscribe(testSubscriber);

        estimateJourney.addOrigin(StopsMother.ANY_STOP);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
    }

    @Test
    public void shouldNotExecuteWhenJustDestinationIsSelected() throws Exception {
        TestSubscriber<List<Estimate>> testSubscriber = new TestSubscriber<>();
        estimateJourney.bind().subscribe(testSubscriber);

        estimateJourney.addDestination(StopsMother.ANY_STOP);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
    }

    @Test
    public void shouldExecuteWhenOriginAndDestinationAreSelected() throws Exception {
        when(estimatesRepository.estimateJourney(StopsMother.ANY_STOPS_LIST))
                .thenReturn(Single.just(EstimatesMother.ANY_ESTIMATE_LIST));

        TestSubscriber<List<Estimate>> testSubscriber = new TestSubscriber<>();
        estimateJourney.bind().subscribe(testSubscriber);

        estimateJourney.addOrigin(StopsMother.ANY_STOP);
        estimateJourney.addDestination(StopsMother.ANY_STOP);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValuesAndClear(EstimatesMother.ANY_ESTIMATE_LIST);
        testSubscriber.assertNotCompleted();
    }

    @Test
    public void shouldExecuteWhenNewOriginOrDestinationAreSelected() throws Exception {
        when(estimatesRepository.estimateJourney(StopsMother.ANY_STOPS_LIST))
                .thenReturn(Single.just(EstimatesMother.ANY_ESTIMATE_LIST));

        TestSubscriber<List<Estimate>> testSubscriber = new TestSubscriber<>();
        estimateJourney.bind().subscribe(testSubscriber);

        estimateJourney.addOrigin(StopsMother.ANY_STOP);
        estimateJourney.addDestination(StopsMother.ANY_STOP);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValuesAndClear(EstimatesMother.ANY_ESTIMATE_LIST);
        testSubscriber.assertNotCompleted();

        estimateJourney.addOrigin(StopsMother.ANY_STOP);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValuesAndClear(EstimatesMother.ANY_ESTIMATE_LIST);
        testSubscriber.assertNotCompleted();

        estimateJourney.addDestination(StopsMother.ANY_STOP);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValuesAndClear(EstimatesMother.ANY_ESTIMATE_LIST);
        testSubscriber.assertNotCompleted();
    }
}
