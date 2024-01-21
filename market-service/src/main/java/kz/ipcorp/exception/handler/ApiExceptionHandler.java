package kz.ipcorp.exception.handler;

import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleApiRequestException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }

    @ExceptionHandler(NotConfirmedException.class)
    public ResponseEntity<?> handleApiRequestException(NotConfirmedException e) {
        HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                e.getMessage(),
                HttpStatus.PRECONDITION_FAILED.value()
        );

        return new ResponseEntity<>(apiExceptionResponse, httpStatus);
    }


}