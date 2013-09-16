package no.bekk;

import no.bekk.busfetcher.BusFetcherMain;
import no.bekk.busfetcher.util.Logger;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            Logger.log("Lets go!");
            BusFetcherMain.runEternally();
        }
        else if(args[0].equals("test")) {
            LedTester.run();
        }
        else {
            System.out.println("You tried to do something, but I couldn't fully understand what");
        }

    }

}
