package no.bekk.busfetcher.scheduler;

import no.bekk.busfetcher.ruter.RuterIOException;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class NextCheckScheduler {

    public static final DateTime START_OF_EXCEPTED_NO_BUS_TIME = new LocalTime(0, 0).toDateTimeToday();
    public static final DateTime END_OF_EXPECTED_NO_BUS_TIME = new LocalTime(5, 0).toDateTimeToday();

    private static NextCheckScheduler instance;
    private Integer lastWaitingTimeInMinutes;
    private Exception lastException;

    public synchronized void storePreviousWaitingTimeInMinutes(int waitingTimeInMinutes) {
        this.lastWaitingTimeInMinutes = waitingTimeInMinutes;
        this.lastException = null;
    }

    public synchronized void storeError(Exception e) {
        this.lastException = e;
        this.lastWaitingTimeInMinutes = null;
    }

    public synchronized long getMillisToSleepBeforeNextCheck() {
        Interval noBusInterval = new Interval(START_OF_EXCEPTED_NO_BUS_TIME, END_OF_EXPECTED_NO_BUS_TIME);
        if (lastWaitingTimeInMinutes == null) {
            // Last lookup failed

            if (noBusInterval.containsNow()) {
                return t(10, MINUTES);
            }

            if (lastException instanceof RuterIOException) {
                // Couldn't connect to Ruter on last attempt. Retry more aggressively.
                return t(30, SECONDS);
            }

            return t(2, MINUTES);
        }

        if (lastWaitingTimeInMinutes > 8) {
            // Sleep until there are 8 minutes till it should be there. Allows for 3 minutes deviation (as we have 5 LEDs)
            return t(lastWaitingTimeInMinutes - 8, MINUTES);
        }

        if (lastWaitingTimeInMinutes > 5) {
            return t(1, MINUTES);
        }

        return t(30, SECONDS);
    }

    private long t(int time, TimeUnit unit) {
        return unit.toMillis(time);
    }

    private NextCheckScheduler() { }

    public static synchronized NextCheckScheduler getInstance() {
        if (instance == null ) {
            instance = new NextCheckScheduler();
        }
        return instance;
    }

}
