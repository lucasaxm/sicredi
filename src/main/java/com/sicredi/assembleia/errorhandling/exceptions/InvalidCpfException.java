package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

public class InvalidCpfException extends Exception{
    public InvalidCpfException(String cpf) {
        super(String.format(ErrorMessages.INVALID_CPF, cpf));
    }
}
