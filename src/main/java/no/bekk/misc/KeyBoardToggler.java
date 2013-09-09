package no.bekk.misc;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class KeyboardToggler {
    public static void run() {
        GpioController gpioController = GpioFactory.getInstance();

        List<Pin> pins = asList(RaspiPin.GPIO_00, RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04);
        List<GpioPinDigitalOutput> digitalPins = new ArrayList<GpioPinDigitalOutput>();

        for (Pin pin : pins) {
            GpioPinDigitalOutput gpioPinDigitalOutput = gpioController.provisionDigitalOutputPin(pin, PinState.LOW);
            digitalPins.add(gpioPinDigitalOutput);
        }

    }


    public static void main(String[] args) {
        while (true) {
            try {
                char readChar = (char) System.in.read();
                switch (readChar) {
                    case (1):

                }

            } catch (IOException e) {
                e.printStackTrace();  //TODO: Implement me!
            }
        }
    }
}
