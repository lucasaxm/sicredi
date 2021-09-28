package com.sicredi.assembleia.errorhandling;

import com.sicredi.assembleia.errorhandling.apierrors.ApiError;
import com.sicredi.assembleia.errorhandling.apierrors.ApiValidationError;
import com.sicredi.assembleia.errorhandling.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CpfUnableToVoteException.class})
    protected ResponseEntity<Object> handleCpfUnableToVoteException(CpfUnableToVoteException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, ex));
    }

    @ExceptionHandler({InvalidCpfException.class})
    protected ResponseEntity<Object> handleInvalidCpfException(InvalidCpfException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_ACCEPTABLE, ex));
    }

    @ExceptionHandler({SessionClosedException.class})
    protected ResponseEntity<Object> handleSessionClosedException(SessionClosedException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_ACCEPTABLE, ex));
    }

    @ExceptionHandler({VotingAgainException.class})
    protected ResponseEntity<Object> handleVotingAgainException(VotingAgainException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, ex));
    }

    @ExceptionHandler({DuplicateKeyException.class})
    protected ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, ex.getCause().getLocalizedMessage(), ex));
    }

    @ExceptionHandler({DataNotFoundException.class})
    protected ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler({NoSearchParametersException.class})
    protected ResponseEntity<Object> handleNoSearchParametersException(NoSearchParametersException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.VALIDATION_ERROR, ex,
                ex.getFieldErrors().stream().map(ApiValidationError::new).collect(Collectors.toList())));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_JSON, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
