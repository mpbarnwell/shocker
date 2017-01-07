package io.stupidgimp.shocker;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.stupidgimp.shocker.io.PinAdapter;
import io.stupidgimp.shocker.resources.ShockerResource;

public class Shocker extends Application<ShockerConfiguration> {

    public static void main(String[] args) throws Exception {
        new Shocker().run(args);
    }

    @Override
    public String getName() {
        return "shocker";
    }

    @Override
    public void initialize(Bootstrap<ShockerConfiguration> bootstrap) {
        // nothing to do yet
    }

    public void run(ShockerConfiguration shockerConfiguration, Environment environment) throws Exception {
        PinAdapter pinAdapter = new PinAdapter(shockerConfiguration.getPinLayout());
        environment.jersey().register(new ShockerResource(pinAdapter));
    }

}
