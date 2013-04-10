package no.bekk.busfetcher.ruter;

class RuterException extends RuntimeException {
    public RuterException(Exception e) {
        super(e);
    }

    public RuterException(String s) {
        super(s);
    }
}
