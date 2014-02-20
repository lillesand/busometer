package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

abstract class LedPattern {

    protected final List<GpioPinDigitalOutput> outputPins;

    public LedPattern(List<GpioPinDigitalOutput> outputPins) {
        this.outputPins = outputPins;
    }

    abstract void render() throws InterruptedException;

    abstract long getRenderLoopDelayMs();

    protected void disableAllLeds() {
        for (GpioPinDigitalOutput outputPin : outputPins) {
            outputPin.setState(PinState.LOW);
        }
    }



}
