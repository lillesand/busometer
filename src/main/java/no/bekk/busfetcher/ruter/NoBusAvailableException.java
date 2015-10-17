package no.bekk.busfetcher.ruter;

public class NoBusAvailableException extends RuterException {
	public NoBusAvailableException(String reason) {
		super(reason);
	}
}
