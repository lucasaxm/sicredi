package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

import java.util.function.Supplier;

public class DataNotFoundException extends Exception implements Supplier<DataNotFoundException> {
    public DataNotFoundException(String id) {
        super(String.format(ErrorMessages.DATA_NOT_FOUND, id));
    }

    @Override
    public DataNotFoundException get() {
        return this;
    }
}
