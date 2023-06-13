package dev.psulej.dataintegrationprojectbackend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationErrorResponse> handleUserException(ApplicationException e) {
        ApplicationErrorResponse response = new ApplicationErrorResponse(e.errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}