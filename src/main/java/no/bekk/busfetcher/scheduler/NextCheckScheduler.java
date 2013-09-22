package no.bekk.busfetcher.scheduler;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class NextCheckScheduler {

    public static final DateTime START_OF_EXCEPTED_NO_BUS_TIME = new LocalTime(0, 0).toDateTimeToday();
    public static final DateTime END_OF_EXPECTED_NO_BUS_TIME = new LocalTime(5, 0).toDateTimeToday();

    private static NextCheckScheduler instance;
    private Integer lastWaitingTimeInMinutes;

    public synchronized void storePreviousWaitingTimeInMinutes(int waitingTimeInMinutes) {
        this.lastWaitingTimeInMinutes = waitingTimeInMinutes;
    }

    public synchronized void storeError() {
       this.lastWaitingTimeInMinutes = null;
    }

    public synchronized long getMillisToSleepBeforeNextCheck() {
        Interval noBusInterval = new Interval(START_OF_EXCEPTED_NO_BUS_TIME, END_OF_EXPECTED_NO_BUS_TIME);
        if (lastWaitingTimeInMinutes == null) {
            // Last lookup failed

            if (noBusInterval.containsNow()) {
                return MINUTES.toMillis(10);
            }

            return MINUTES.toMillis(2);
        }

        if (lastWaitingTimeInMinutes > 8) {
            // Sleep until there are 8 minutes till it should be there. Allows for 3 minutes deviation
            return MINUTES.toMillis(lastWaitingTimeInMinutes - 8);
        }

        if (lastWaitingTimeInMinutes > 5) {
            return MINUTES.toMillis(1);
        }

        return SECONDS.toMillis(30);
    }



    private NextCheckScheduler() { }

    public static synchronized NextCheckScheduler getInstance() {
        if (instance == null ) {
            instance = new NextCheckScheduler();
        }
        return instance;
    }

}
