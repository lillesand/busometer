package no.bekk;

import no.bekk.busfetcher.BusFetcherMain;
import no.bekk.busfetcher.raspi.LedController;
import no.bekk.busfetcher.util.Logger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            Logger.log("Lets go!");
            BusFetcherMain.runEternally();
        }
        else if (args[0].equals("test")) {

            Logger.log("Default state (typically on load)");
            LedController ledController = new LedController();
            Thread.sleep(5000);

            Logger.log("Error");
            ledController.showError();
            Thread.sleep(5000);

            Logger.log("Number 4");
            ledController.showNumber(4);
            Thread.sleep(5000);

            ledController.stop();
        }
        else {
            Logger.log("You tried to do something, but I couldn't fully understand what");
        }

    }

}
