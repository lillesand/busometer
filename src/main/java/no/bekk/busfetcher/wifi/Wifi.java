package no.bekk.busfetcher.wifi;

import no.bekk.busfetcher.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Wifi {

	private final File wifiRestartScript;

	public Wifi() {
		this.wifiRestartScript = new File("/usr/local/bin/wifi_check");
		if (!wifiRestartScript.canExecute()) {
			Logger.log("ERROR: Can not execute wifi restart script at " + this.wifiRestartScript + ".\n" +
					"Wifi will not be restarted automatically.");
		}
		else {
			Logger.log("Will verify wifi using " + wifiRestartScript);
		}
	}

	public void verifyUp() {
		if (!wifiRestartScript.canExecute()) {
			return;
		}

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(wifiRestartScript.getAbsolutePath());
			Process process = processBuilder.start();
			process.waitFor(15, TimeUnit.SECONDS);

			Runtime.getRuntime().exec(wifiRestartScript.getAbsolutePath());
		} catch (IOException e) {
			Logger.log("Unable to restart Wifi because of " + e.getClass().getSimpleName() + ": " + e.getMessage());
		} catch (InterruptedException e) {
			Logger.log("Wifi restart did not finish in time. Proceeding anyway.");
		}
	}
}
