package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

public class NotUniqueException extends Exception {
    public NotUniqueException(String field) {
        super(String.format(ErrorMessages.UNIQUE, field));
    }
}
