package no.bekk.misc;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class LedConfigTester {
    public static void runEternally() throws InterruptedException {
        GpioController gpioController = GpioFactory.getInstance();

        List<Pin> pins = asList(RaspiPin.GPIO_00, RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04);
        List<GpioPinDigitalOutput> digitalPins = new ArrayList<GpioPinDigitalOutput>();

        for (Pin pin : pins) {
            GpioPinDigitalOutput gpioPinDigitalOutput = gpioController.provisionDigitalOutputPin(pin, PinState.LOW);
            digitalPins.add(gpioPinDigitalOutput);
        }

        while (true) {
            System.out.println("Lights on!");
            for (GpioPinDigitalOutput digitalPin : digitalPins) {
                digitalPin.setState(PinState.HIGH);
            }
            Thread.sleep(500);

            System.out.println("Lights out…");
            for (GpioPinDigitalOutput digitalPin : digitalPins) {
                digitalPin.setState(PinState.LOW);
            }
            Thread.sleep(500);
        }
    }
}
