package no.bekk.busfetcher;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import no.bekk.busfetcher.scheduler.NextCheckScheduler;
import no.bekk.busfetcher.util.Logger;

public class Main {

    public static void main(String[] args) {
        Logger.log("Lets go!");
        runEternally();
    }

    public static void runEternally() {
        BusTimeLedEmitter busTimeLedEmitter = new BusTimeLedEmitter();
        NextCheckScheduler scheduler = NextCheckScheduler.getInstance();
        while (true) {
            try {
                busTimeLedEmitter.showTimeToNextDepartureInMinutesOnLed();
            }
            catch (RuntimeException e) {
                scheduler.storeError();
                Logger.log("ERROR: Caught a " + e.getClass() + " because " + e.getMessage());
            }

            try {
                long millisToSleep = scheduler.getMillisToSleepBeforeNextCheck();
				Logger.log(String.format("Sleeping %s minutes (%s seconds) before checking again.", MILLISECONDS.toMinutes(millisToSleep), MILLISECONDS.toSeconds(millisToSleep)));
                sleep(millisToSleep);
            } catch (InterruptedException e) {
				Logger.log("ERROR: Got interrupted while sleeping: " + e.getMessage());
            }
        }
    }
}
