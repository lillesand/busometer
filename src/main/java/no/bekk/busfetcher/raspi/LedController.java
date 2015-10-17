package no.bekk.busfetcher.raspi;

import no.bekk.busfetcher.ruter.NoBusAvailableException;

public class LedController {

    private ThreadLedController ledControllerThread;

    public LedController() {
        ledControllerThread = new ThreadLedController();
        Thread thread = new Thread(ledControllerThread);
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ledControllerThread.stopRunning();
            }
        });
    }


    public void showError(Exception e) {
        if (e instanceof NoBusAvailableException) {
			ledControllerThread.dancingLeds();
		}
		else {
			ledControllerThread.blinkLeds();
		}
	}

    public void showNumber(int number) {
        ledControllerThread.showNumber(number);
    }

    public void disableAllLeds() {
        ledControllerThread.showNumber(0);
    }

	public void test() {
		ledControllerThread.dancingLeds();
	}

    public void stop() {
        ledControllerThread.stopRunning();
    }

}
