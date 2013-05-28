package no.bekk.busfetcher.scheduler;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class NextCheckScheduler {

    private static NextCheckScheduler instance;
    private Integer lastWaitingTimeInMinutes;

    public synchronized void storePreviousWaitingTimeInMinutes(int waitingTimeInMinutes) {
        this.lastWaitingTimeInMinutes = waitingTimeInMinutes;
    }

    public synchronized void storeError() {
       this.lastWaitingTimeInMinutes = null;
    }

    public synchronized long getMillisToSleepBeforeNextCheck() {
        if (lastWaitingTimeInMinutes == null) {
            // Last lookup failed, let's not over-do it
            return MINUTES.toMillis(10);
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
