package no.bekk;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

import java.util.ArrayList;
import java.util.List;

public class LedTester {
    public static void run() {
    }

    private static List<GpioPinDigitalOutput> listOfProvisionedPins() {
        GpioController gpioController = GpioFactory.getInstance();

        List<GpioPinDigitalOutput> pins = new ArrayList<GpioPinDigitalOutput>();
        pins.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00));
        pins.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01));
        pins.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02));
        pins.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03));
        pins.add(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04));
        return pins;
    }
}
