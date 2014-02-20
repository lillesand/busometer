package no.bekk.busfetcher.scheduler;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalTime;
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
    public void should_wait_two_minutes_on_error_during_the_day() {
        long tenPastFiveInTheMorning = new LocalTime(5, 10).toDateTimeToday().getMillis();
        DateTimeUtils.setCurrentMillisFixed(tenPastFiveInTheMorning);

        sut.storeError();

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(2, TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void should_wait_10_minutes_when_last_was_error_and_it_is_late_at_night() {
        long tenPastMidnight = new LocalTime(0, 10).toDateTimeToday().getMillis();
        DateTimeUtils.setCurrentMillisFixed(tenPastMidnight);

        sut.storeError();

        long millis = sut.getMillisToSleepBeforeNextCheck();

        assertEquals(10, TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Test
    public void should_wait_10_minutes_when_last_was_error_and_it_is_early_morning() {
        long tenToFiveInTheMorning = new LocalTime(4, 50).toDateTimeToday().getMillis();
        DateTimeUtils.setCurrentMillisFixed(tenToFiveInTheMorning);

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
