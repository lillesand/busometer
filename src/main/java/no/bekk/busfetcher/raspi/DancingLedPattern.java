package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

class DancingLedPattern extends LedPattern {

    private boolean firstRun = true;

    public DancingLedPattern(List<GpioPinDigitalOutput> outputPins) {
        super(outputPins);
    }

    @Override
    public void render() throws InterruptedException {
        for (int i = 0; i < outputPins.size(); i++) {
            Thread.sleep(300);
            disableAllLeds();
            outputPins.get(i).setState(PinState.HIGH);
        }

        // Runs from 2nd to 4th pin, as 1st and 5th are lit by the previous loop
        int fourthPin = outputPins.size() - 2;
        for (int i = fourthPin; i >= 1; i--) {
            Thread.sleep(300);
            disableAllLeds();
            outputPins.get(i).setState(PinState.HIGH);
        }

        if(firstRun) {
            // Light last led in the first run, as it miiight be a single run.
            Thread.sleep(300);
            outputPins.get(fourthPin + 1).setState(PinState.HIGH);
            firstRun = false;
        }

    }

    @Override
    public long getRenderLoopDelayMs() {
        return 0;
    }
}
