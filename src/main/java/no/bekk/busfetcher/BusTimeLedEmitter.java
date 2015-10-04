package no.bekk.busfetcher;

import no.bekk.busfetcher.raspi.LedController;
import no.bekk.busfetcher.ruter.RuterService;
import no.bekk.busfetcher.ruter.UpcomingDepartureToDowntown;
import no.bekk.busfetcher.scheduler.NextCheckScheduler;
import no.bekk.busfetcher.util.Logger;
import no.bekk.busfetcher.wifi.Wifi;

public class BusTimeLedEmitter {

	private final LedController ledController;
	private final RuterService ruterService;
	private final NextCheckScheduler nextCheckScheduler;
	private final Wifi wifi;

	public BusTimeLedEmitter() {
		wifi = new Wifi();
        ruterService = new RuterService();
        ledController = new LedController();
        nextCheckScheduler = NextCheckScheduler.getInstance();
    }

    public void showTimeToNextDepartureInMinutesOnLed() {
		ledController.disableAllLeds();
		wifi.verifyUp();

		try {
			UpcomingDepartureToDowntown upcomingDepartureToDowntown = ruterService.fetchRealtimeInformation();

			int waitingTimeInMinutes = upcomingDepartureToDowntown.getWaitingTimeInMinutes();
			nextCheckScheduler.storePreviousWaitingTimeInMinutes(waitingTimeInMinutes);

			Logger.log(upcomingDepartureToDowntown);
			ledController.showNumber(waitingTimeInMinutes);
		}
		catch (RuntimeException e) {
			ledController.showError();
			throw e;
		}
    }

}
