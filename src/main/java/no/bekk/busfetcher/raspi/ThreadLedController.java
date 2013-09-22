package no.bekk.busfetcher.raspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.List;

import static java.util.Arrays.asList;

class ThreadLedController implements Runnable {

    private final List<GpioPinDigitalOutput> outputPins;

    private boolean running;
    private boolean error;
    private Integer number;

    public ThreadLedController() {
        GpioController gpioController = GpioFactory.getInstance();

        this.outputPins = asList(
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW),
                gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW)
        );

        this.running = true;
    }

    public void stopRunning() {
        synchronized (outputPins) {
            this.running = false;
            disableAllLeds();
        }
    }

    public void showError() {
        synchronized (outputPins) {
            this.number = null;
            this.error = true;
        }
    }

    public void showNumber(int number) {
        synchronized (outputPins) {
            this.error = false;
            this.number = number;
        }
    }

    @Override
    public void run() {
        Integer previousNumber = number;
        while (running) {
            try {
                synchronized (outputPins) {
                    if (number != null && !number.equals(previousNumber)) {
                        lightConsecutiveLeds(number);
                        Thread.sleep(500);
                    }
                    else if (error) {
                        danceLedsOnce();
                    }
                    else {
                        danceLedsOnce();
                    }
                }
            } catch (InterruptedException e) {
                // That's okay
            }
        }

        disableAllLeds();
    }

    private void danceLedsOnce() throws InterruptedException {
        for (int i = 0; i < outputPins.size(); i++) {
            Thread.sleep(300);
            disableAllLeds();
            outputPins.get(i).setState(PinState.HIGH);
        }

        // Runs from 2nd to 4th pin, as 1st and 5th are lit by the previous loop
        int fourthPin = outputPins.size() - 2;
        for (int i = fourthPin; i >= 1; i--) {
            Thread.sleep(300);
            disableAllLeds();
            outputPins.get(i).setState(PinState.HIGH);
        }
    }

    private void disableAllLeds() {
        for (GpioPinDigitalOutput outputPin : outputPins) {
            outputPin.setState(PinState.LOW);
        }
    }

    private void lightConsecutiveLeds(int number) {
        disableAllLeds();
        if (number > outputPins.size()) {
            number = outputPins.size();
        }

        for (int i = 0; i < number; i++) {
            outputPins.get(i).setState(PinState.HIGH);
        }
    }

}
