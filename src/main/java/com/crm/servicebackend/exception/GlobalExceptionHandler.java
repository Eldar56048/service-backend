package com.crm.servicebackend.exception;

import com.crm.servicebackend.exception.domain.AuthException;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        LOGGER.error(String.valueOf(exception));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), new Date(), exception.getMessage(), request.getDescription(false), exception.getCode());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception, WebRequest request) {
        LOGGER.error(String.valueOf(exception.getMessage()));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), new Date(), "Невозможно удалить так как он уже используется", request.getDescription(false), "data/not-delete");
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException exception, WebRequest request) {
        LOGGER.error(String.valueOf(exception));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), new Date(), exception.getMessage(), request.getDescription(false), exception.getCode());
        return new ResponseEntity(errorDetails, HttpStatus.UNAUTHORIZED);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        LOGGER.error(String.valueOf(ex));
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), new Date(), "Валидация провалена", request.getDescription(false), "validation/failed");
        ValidationErrorDetails validationErrorDetails = new ValidationErrorDetails(errorDetails, errors);
        return new ResponseEntity(validationErrorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DtoException.class)
    public ResponseEntity<?> handleDtoException(DtoException exception, WebRequest request) {
        LOGGER.error(String.valueOf(exception));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), request.getDescription(false), exception.getCode());
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, WebRequest request) {
        LOGGER.error(String.valueOf(exception));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.METHOD_NOT_ALLOWED.value(), new Date(), "Метод запроса "+exception.getMethod()+" не поддерживается", request.getDescription(false), "method/not-allowed");
        return new ResponseEntity(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
