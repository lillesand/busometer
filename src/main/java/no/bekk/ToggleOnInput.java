package no.bekk;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Hello world!
 *
 */
public class ToggleOnInput
{
    public static void main(String[] args) throws Exception {
        System.out.println("Let's toggle!");

        GpioController gpioController = GpioFactory.getInstance();

        final GpioPinDigitalOutput outputPin00 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "IO0", PinState.LOW);
        GpioPinDigitalInput inputPin01 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01);

        inputPin01.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("I'm touched!");
                outputPin00.setState(event.getState());
            }
        });


        System.in.read();
    }
}
