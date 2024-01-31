package kz.ipcorp.exception.handler;

import kz.ipcorp.exception.AuthenticationException;
import kz.ipcorp.exception.DuplicateEntityException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.exception.SMSException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<?> handleApiNotFoundException(NotFoundException e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                e.getMessage(),
                httpStatus.value()
        );

        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

    @ExceptionHandler(value = {DuplicateEntityException.class})
    public ResponseEntity<?> handleApiDuplicateEntityException(DuplicateEntityException d){
        HttpStatus httpStatus = HttpStatus.FOUND;

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                d.getMessage(),
                httpStatus.value()
        );
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

    @ExceptionHandler(value = {SMSException.class})
    public ResponseEntity<?> handleApiSMSException(SMSException s){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                s.getMessage(),
                httpStatus.value()
        );
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException a){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                a.getMessage(),
                httpStatus.value()
        );
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }
}