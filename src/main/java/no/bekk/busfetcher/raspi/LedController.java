package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.*;

import java.util.List;

import static java.util.Arrays.asList;

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
        setAllLow();
        if (number > outputPins.size()) {
            number = outputPins.size();
        }

        for (int i = 0; i < number; i++) {
            outputPins.get(i).setState(PinState.HIGH);
        }
    }

    private void setAllLow() {
        for (GpioPinDigitalOutput outputPin : outputPins) {
            outputPin.setState(PinState.LOW);
        }
    }
}
