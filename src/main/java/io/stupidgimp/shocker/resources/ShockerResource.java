package io.stupidgimp.shocker.resources;

import io.stupidgimp.shocker.io.PinAdapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static java.util.Objects.requireNonNull;

@Path("control")
public class ShockerResource {

    private final PinAdapter pinAdapter;

    public ShockerResource(PinAdapter pinAdapter) {
        this.pinAdapter = requireNonNull(pinAdapter);
    }

    @GET
    @Path("power/increment")
    public void incrementPower() {
        pinAdapter.incrementPower();
    }

    @GET
    @Path("power/decrement")
    public void decrementPower() {
        pinAdapter.decrementPower();
    }

    @GET
    @Path("mode")
    public void toggleMode() {
        pinAdapter.switchMode();
    }

    @GET
    @Path("channel")
    public void toggleChannel() {
        pinAdapter.switchChannel();
    }

    @GET
    @Path("launch")
    public void launch() {
        pinAdapter.launch();
    }

}
