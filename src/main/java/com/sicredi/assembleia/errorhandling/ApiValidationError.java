package com.sicredi.assembleia.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.FieldError;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    ApiValidationError(FieldError fieldError, String message){
        this.object = fieldError.getObjectName();
        this.field = fieldError.getField();
        this.rejectedValue = fieldError.getRejectedValue();
        this.message = message;
    }

    public ApiValidationError(FieldError fieldError){
        this.object = fieldError.getObjectName();
        this.field = fieldError.getField();
        this.rejectedValue = fieldError.getRejectedValue();
        this.message = fieldError.getDefaultMessage();
    }
}
