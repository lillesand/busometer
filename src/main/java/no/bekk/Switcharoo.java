package no.bekk;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

/**
 * Hello world!
 *
 */
public class Switcharoo
{
    public static void main(String[] args) throws Exception {
        System.out.println("Let's switch!");

        GpioController gpioController = GpioFactory.getInstance();

        final List<GpioPinDigitalOutput> outputPins = asList(
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "IO0", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "IO1", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, "IO2", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, "IO3", PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "IO4", PinState.LOW)
        );

        GpioPinDigitalInput inputPin05 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_05);
        inputPin05.setPullResistance(PinPullResistance.PULL_UP);

        final AtomicInteger lastActive = new AtomicInteger(0);
        inputPin05.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                for (GpioPinDigitalOutput outputPin : outputPins) {
                    outputPin.low();
                }
                outputPins.get(lastActive.intValue() % (outputPins.size())).high();

                lastActive.incrementAndGet();
            }
        });


        System.in.read();
    }
}
