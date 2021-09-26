package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

public class NoSearchParametersException extends Exception {
    public NoSearchParametersException() {
        super(ErrorMessages.NO_SEARCH_PARAMS);
    }
}
