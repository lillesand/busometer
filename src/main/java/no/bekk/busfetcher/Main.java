package no.bekk.busfetcher;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {

    public static void main(String[] args) {
        System.out.println("Lets go!");
        runEternally();
    }

    public static void runEternally() {
        BusTimeLedEmitter busTimeLedEmitter = new BusTimeLedEmitter();
        while (true) {
            try {
                busTimeLedEmitter.showTimeToNextDepartureInMinutesOnLed();
            }
            catch (RuntimeException e) {
                System.out.println("ERROR: Caught a " + e.getClass() + " because " + e.getMessage());
            }

            try {
                Thread.sleep(SECONDS.toMillis(55));
            } catch (InterruptedException e) {
                System.out.println("ERROR: Got interrupted while sleeping: " + e.getMessage());
            }
        }
    }
}
