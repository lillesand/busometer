package no.bekk.busfetcher.raspi;

import static java.util.Arrays.asList;

import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LedController {

    private List<GpioPinDigitalOutput> outputPins;

    public LedController() {
        GpioController gpioController = GpioFactory.getInstance();

        outputPins = asList(
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "IO0", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "IO1", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, "IO2", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, "IO3", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "IO4", PinState.LOW)
        );
    }

    public void showNumber(int number) {
        disableAllLeds();
        if (number > outputPins.size()) {
            number = outputPins.size();
        }

        for (int i = 0; i < number; i++) {
            outputPins.get(i).setState(PinState.HIGH);
        }
    }

    public void disableAllLeds() {
		// Disable any blinking
		blinkAll(0);
        for (GpioPinDigitalOutput outputPin : outputPins) {
            outputPin.setState(PinState.LOW);
        }
    }

	public void showError() {
		blinkAll(2000);
	}

	private void blinkAll(final int delay) {
		for (GpioPinDigitalOutput outputPin : outputPins) {
			outputPin.blink(delay);
		}
	}
}
