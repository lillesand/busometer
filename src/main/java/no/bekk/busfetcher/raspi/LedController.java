package no.bekk.busfetcher.raspi;

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


	public void showError() {
        ledControllerThread.showError();
	}

    public void showNumber(int number) {
        ledControllerThread.showNumber(number);
    }

    public void disableAllLeds() {
        ledControllerThread.showNumber(0);
    }

    public void stop() {
        ledControllerThread.stopRunning();
    }


}
