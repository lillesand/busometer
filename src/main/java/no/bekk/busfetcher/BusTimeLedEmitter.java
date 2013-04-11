package no.bekk.busfetcher;

import no.bekk.busfetcher.raspi.LedController;
import no.bekk.busfetcher.ruter.RuterService;
import no.bekk.busfetcher.ruter.UpcomingDepartureToDowntown;
import no.bekk.busfetcher.scheduler.NextCheckScheduler;

public class BusTimeLedEmitter {

    private final LedController ledController;
    private final RuterService ruterService;
    private final NextCheckScheduler nextCheckScheduler;

    public BusTimeLedEmitter() {
        ruterService = new RuterService();
        ledController = new LedController();
        nextCheckScheduler = NextCheckScheduler.getInstance();
    }

    public void showTimeToNextDepartureInMinutesOnLed() {
        ledController.disableAllLeds();
        UpcomingDepartureToDowntown upcomingDepartureToDowntown = ruterService.fetchRealtimeInformation();

        int waitingTimeInMinutes = upcomingDepartureToDowntown.getWaitingTimeInMinutes();
        nextCheckScheduler.storePreviousWaitingTimeInMinutes(waitingTimeInMinutes);

        System.out.println(upcomingDepartureToDowntown);
        ledController.showNumber(waitingTimeInMinutes);
    }

}
