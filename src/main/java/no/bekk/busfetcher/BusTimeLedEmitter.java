package no.bekk.busfetcher;

import no.bekk.busfetcher.raspi.LedController;
import no.bekk.busfetcher.ruter.RuterService;
import no.bekk.busfetcher.ruter.UpcomingDepartureToDowntown;

public class BusTimeLedEmitter {

    private final LedController ledController;
    private final RuterService ruterService;

    public BusTimeLedEmitter() {
        ruterService = new RuterService();
        ledController = new LedController();
    }

    public void showTimeToNextDepartureInMinutesOnLed() {
        ledController.disableAllLeds();
        UpcomingDepartureToDowntown upcomingDepartureToDowntown = ruterService.fetchRealtimeInformation();
        int waitingTimeInMinutes = upcomingDepartureToDowntown.getWaitingTimeInMinutes();

        System.out.println(upcomingDepartureToDowntown);
        ledController.showNumber(waitingTimeInMinutes);
    }

}
