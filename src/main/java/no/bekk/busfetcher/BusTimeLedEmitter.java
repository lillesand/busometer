package no.bekk.busfetcher;

import static java.lang.Runtime.getRuntime;

import no.bekk.busfetcher.raspi.LedController;
import no.bekk.busfetcher.ruter.RuterService;
import no.bekk.busfetcher.ruter.UpcomingDepartureToDowntown;
import no.bekk.busfetcher.scheduler.NextCheckScheduler;
import no.bekk.busfetcher.util.Logger;

public class BusTimeLedEmitter {

    private final LedController ledController;
    private final RuterService ruterService;
    private final NextCheckScheduler nextCheckScheduler;

    public BusTimeLedEmitter() {
        ruterService = new RuterService();
        ledController = new LedController();
        nextCheckScheduler = NextCheckScheduler.getInstance();

		getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				ledController.disableAllLeds();
			}
		});
    }

    public void showTimeToNextDepartureInMinutesOnLed() {
        ledController.disableAllLeds();
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
