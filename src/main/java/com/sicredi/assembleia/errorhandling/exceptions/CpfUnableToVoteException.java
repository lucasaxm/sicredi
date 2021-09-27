package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

public class CpfUnableToVoteException extends Exception{
    public CpfUnableToVoteException(String cpf) {
        super(String.format(ErrorMessages.CPF_UNABLE_TO_VOTE, cpf));
    }
}
