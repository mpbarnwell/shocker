package io.stupidgimp.shocker.exception;

import javax.ws.rs.ClientErrorException;

public class PendingOperationException extends ClientErrorException {

    public PendingOperationException(final String message) {
        super(message, 429);
    }

}
