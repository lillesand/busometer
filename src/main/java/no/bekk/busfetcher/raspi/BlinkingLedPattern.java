package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

public class BlinkingLedPattern extends LedPattern {

	private boolean ledsOn;

	public BlinkingLedPattern(List<GpioPinDigitalOutput> outputPins) {
		super(outputPins);
	}

	@Override
	void render() throws InterruptedException {
		if (ledsOn) disableAllLeds();
		else enableAllLeds();
		ledsOn = !ledsOn;
	}

	private void enableAllLeds() {
		for (GpioPinDigitalOutput outputPin : outputPins) {
			outputPin.setState(PinState.HIGH);
		}
	}

	@Override
	long getRenderLoopDelayMs() {
		return 650;
	}
}
