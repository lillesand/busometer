package no.bekk.busfetcher.scheduler;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

public class NextCheckSchedulerTest {

    private NextCheckScheduler sut;

    @Before
    public void setUp() {
        sut = NextCheckScheduler.getInstance();
    }

    @Test
    public void should_always_wait_10_minutes_when_last_was_error() {
        sut.storeError();

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(10, TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void should_sleep_till_8_minutes_remaining_if_longer_than_8() {
        sut.storePreviousWaitingTimeInMinutes(10);

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(2, TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void should_sleep_till_8_minutes_remaining_if_longer_than_8_() {
        sut.storePreviousWaitingTimeInMinutes(17);

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(9, TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void should_sleep_1_minute_if_less_than_8_minutes_remaining() {
        sut.storePreviousWaitingTimeInMinutes(6);

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(1, TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void should_sleep_30_seconds_if_less_than_5_minutes_remaining() {
        sut.storePreviousWaitingTimeInMinutes(5);

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(30, TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS));
    }

}
