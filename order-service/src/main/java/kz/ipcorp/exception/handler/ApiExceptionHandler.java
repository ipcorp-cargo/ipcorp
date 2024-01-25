package kz.ipcorp.exception.handler;

import kz.ipcorp.exception.DuplicatedEntityException;
import kz.ipcorp.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<?> handleDuplicatedEntityException(DuplicatedEntityException e){
        HttpStatus httpStatus = HttpStatus.ALREADY_REPORTED;
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                e.getMessage(),
                HttpStatus.ALREADY_REPORTED.value()
        );
        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }
}