package io.stupidgimp.shocker.io;

import com.doordeck.simplegpio.Signal;
import com.doordeck.simplegpio.gpio.DigitalOutput;
import com.doordeck.simplegpio.platform.PinFactory;
import com.doordeck.simplegpio.spi.InputPollerFactory;
import com.doordeck.simplegpio.spi.InputPollerFactoryLocator;
import com.google.common.util.concurrent.Uninterruptibles;
import io.dropwizard.util.Duration;
import io.stupidgimp.shocker.configuration.PinConfiguration;
import io.stupidgimp.shocker.exception.PendingOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class PinAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(PinAdapter.class);
    private static final Signal PRESSED_SIGNAL = Signal.Low;
    private static final Signal DEPRESSED_SIGNAL = Signal.High;

    private final Duration pressTime;

    private final DigitalOutput powerIncrementPin;
    private final DigitalOutput powerDecrementPin;
    private final DigitalOutput launchPin;
    private final DigitalOutput channelPin;
    private final DigitalOutput modePin;

    private final AtomicBoolean mutex = new AtomicBoolean(false);

    public PinAdapter(PinConfiguration configuration) {
        this.pressTime = requireNonNull(configuration).operation();

        InputPollerFactory inputPollerFactory = InputPollerFactoryLocator.locate();
        PinFactory pinFactory = new PinFactory(inputPollerFactory);

        this.powerDecrementPin = pinFactory.getPin(configuration.powerDecrement()).as(DigitalOutput.class);
        this.powerIncrementPin = pinFactory.getPin(configuration.powerIncrement()).as(DigitalOutput.class);
        this.launchPin = pinFactory.getPin(configuration.launch()).as(DigitalOutput.class);
        this.channelPin = pinFactory.getPin(configuration.channel()).as(DigitalOutput.class);
        this.modePin = pinFactory.getPin(configuration.mode()).as(DigitalOutput.class);

        reset();
    }

    private void reset() {
        // Set all pins to high value
        Stream.of(powerIncrementPin, powerDecrementPin, launchPin, channelPin, modePin)
                .forEach(o -> o.applySignal(DEPRESSED_SIGNAL));
    }

    private void operate(DigitalOutput output) {
        if (!mutex.compareAndSet(false, true)) {
            throw new PendingOperationException("Unable to get mutex");
        }

        LOG.debug("Applying {} signal to GPIO {} for {}ms", PRESSED_SIGNAL, output.getPin().getAddress(), pressTime.toMilliseconds());

        try {
            output.applySignal(PRESSED_SIGNAL);
            Uninterruptibles.sleepUninterruptibly(pressTime.toMilliseconds(), TimeUnit.MILLISECONDS);
            output.applySignal(DEPRESSED_SIGNAL);
        } finally {
            mutex.set(false);
        }
    }

    public void decrementPower() {
        operate(powerDecrementPin);
    }

    public void incrementPower() {
        operate(powerIncrementPin);
    }

    public void launch() {
        operate(launchPin);
    }

    public void switchChannel() {
        operate(channelPin);
    }

    public void switchMode() {
        operate(modePin);
    }

}
