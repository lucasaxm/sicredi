package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

public class SessionClosedException extends Exception{
    public SessionClosedException(String id) {
        super(String.format(ErrorMessages.SESSION_CLOSED, id));
    }
}
