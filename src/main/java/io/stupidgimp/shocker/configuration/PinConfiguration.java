package io.stupidgimp.shocker.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.util.Duration;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePinConfiguration.class)
@JsonDeserialize(as = ImmutablePinConfiguration.class)
public interface PinConfiguration {

    int powerIncrement();
    int powerDecrement();
    int launch();
    int channel();
    int mode();

    @Value.Default
    default Duration operation() {
        return Duration.milliseconds(50);
    }

}
