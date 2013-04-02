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

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Hello world!
 *
 */
public class VerdensFetesteTermometer
{
    public static void main(String[] args) throws Exception {
        System.out.println("Let's termify!");

        GpioController gpioController = GpioFactory.getInstance();


        final GpioPinDigitalOutput thermistorOutput = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, "IO0", PinState.LOW);
        GpioPinDigitalInput getsPowerWhenCapacitorCharged = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01);

        GpioPinDigitalInput button = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_05);
        button.setPullResistance(PinPullResistance.PULL_UP);

        final AtomicLong startTime = new AtomicLong();
        
        button.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (thermistorOutput.getState() == PinState.LOW) {
                    thermistorOutput.high();
                    startTime.set(nowTime());
                }
            }
        });
        
        getsPowerWhenCapacitorCharged.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState() == PinState.HIGH) {
                    long timeElapsed = nowTime() - startTime.get();
                    System.out.println(new BigDecimal(timeElapsed).divide(new BigDecimal(1000)));

                    thermistorOutput.low();
                }
            }
        });

        System.in.read();
    }

    private static long nowTime() {
        return System.nanoTime();
    }
}
