package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

public class NumberLedsPattern extends LedPattern {

    private final int number;
    private boolean numberRendered = false;

    public NumberLedsPattern(List<GpioPinDigitalOutput> outputPins, int number) {
        super(outputPins);
        this.number = number;
    }

    @Override
    void render() throws InterruptedException {
        if (numberRendered) {
            return;
        }

        numberRendered = true;

        disableAllLeds();
        int ledsToLight = number;
        if (number > outputPins.size()) {
            ledsToLight = outputPins.size();
        }

        for (int i = 0; i < ledsToLight; i++) {
            outputPins.get(i).setState(PinState.HIGH);
        }
    }

    @Override
    long getRenderLoopDelayMs() {
        return 250;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return number == ((NumberLedsPattern) o).number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
