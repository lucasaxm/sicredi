package com.sicredi.assembleia.errorhandling.exceptions;

import com.sicredi.assembleia.errorhandling.ErrorMessages;

public class VotingAgainException extends Exception{
    public VotingAgainException(String id) {
        super(String.format(ErrorMessages.VOTING_AGAIN, id));
    }
}
