package no.bekk.busfetcher;

import no.bekk.busfetcher.scheduler.NextCheckScheduler;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Main {

    public static void main(String[] args) {
        System.out.println("Lets go!");
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
                System.out.println("ERROR: Caught a " + e.getClass() + " because " + e.getMessage());
            }

            try {
                long millisToSleep = scheduler.getMillisToSleepBeforeNextCheck();
                System.out.println(String.format("Sleeping %s minutes (%s seconds) before checking again.", MILLISECONDS.toMinutes(millisToSleep), MILLISECONDS.toSeconds(millisToSleep)));
                sleep(millisToSleep);
            } catch (InterruptedException e) {
                System.out.println("ERROR: Got interrupted while sleeping: " + e.getMessage());
            }
        }
    }
}
