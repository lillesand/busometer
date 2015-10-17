package no.bekk.busfetcher.ruter;

import java.io.IOException;

public class RuterIOException extends RuterException {
	public RuterIOException(IOException e) {
		super(e);
	}
}
