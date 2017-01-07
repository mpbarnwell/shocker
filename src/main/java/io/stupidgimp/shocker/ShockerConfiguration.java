package io.stupidgimp.shocker;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.stupidgimp.shocker.configuration.PinConfiguration;

import javax.validation.constraints.NotNull;

public class ShockerConfiguration extends Configuration {

    @JsonProperty
    @NotNull
    private PinConfiguration pinLayout;

    public PinConfiguration getPinLayout() {
        return pinLayout;
    }

}
