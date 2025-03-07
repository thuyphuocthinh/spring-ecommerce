package com.tpt.tpt_ecom.exceptions;

import com.tpt.tpt_ecom.dto.APIErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
// Global Exception Handler
// Handles all exceptions thrown from controller
public class GlobalExceptionHandler {
    // all MethodArgumentNotValidException thrown from any rest controller
    // will be handled by this method
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentsNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = ((FieldError)error).getField();
            response.put(fieldName, errorMessage);
        });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIErrorResponse> myResourceNotFoundException(ResourceNotFoundException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse(
                false,
                ex.getMessage()
        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIErrorResponse> apiException(APIException ex) {
        APIErrorResponse apiErrorResponse = new APIErrorResponse(
            false,
            ex.getMessage()
    );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<APIErrorResponse> ioException(IOException ex) {
        return new ResponseEntity<>(new APIErrorResponse(false, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

/*
* Response Success Standard
* Response Error Standard
* */