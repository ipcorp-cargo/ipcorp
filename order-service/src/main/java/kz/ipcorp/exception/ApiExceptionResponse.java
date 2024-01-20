package kz.ipcorp.exception;


import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message, Throwable throwable, ZonedDateTime time) {
}
