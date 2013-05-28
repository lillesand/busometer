package no.bekk.busfetcher.util;

import org.joda.time.DateTime;

public class Logger {

	public static void log(Object message) {
		System.out.println(new DateTime().toString("yyyy-MM-dd HHmm: ") + message);
	}

}
