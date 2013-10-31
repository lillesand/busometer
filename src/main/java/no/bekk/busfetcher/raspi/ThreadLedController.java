package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import no.bekk.busfetcher.util.Logger;

import java.util.List;

import static java.util.Arrays.asList;

class ThreadLedController implements Runnable {

    private final List<GpioPinDigitalOutput> outputPins;

    private boolean running;
    private LedPattern ledPattern;

    public ThreadLedController() {
        GpioController gpioController = GpioFactory.getInstance();

        this.outputPins = asList(
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW)
        );

        this.ledPattern = new DancingLedPattern(outputPins);
        this.running = true;
    }

    public void stopRunning() {
        this.running = false;
    }

    public void showError() {
        this.ledPattern = new DancingLedPattern(outputPins);
    }

    public void showNumber(int number) {
        NumberLedsPattern newPattern = new NumberLedsPattern(outputPins, number);
        if (!newPattern.equals(this.ledPattern)) {
            this.ledPattern = newPattern;

        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                this.ledPattern.render();
                Thread.sleep(this.ledPattern.getRenderLoopDelayMs());
            }
            catch (InterruptedException e) {
                // That's okay
            }
            catch (RuntimeException e) {
                Logger.log("Caught " + e.getClass() + " while trying to control lights. Cause: " + e.getMessage());
            }
        }

        new NumberLedsPattern(outputPins, 0).render();
    }

}
