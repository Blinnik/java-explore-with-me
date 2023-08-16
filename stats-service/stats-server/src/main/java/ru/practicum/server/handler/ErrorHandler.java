package ru.practicum.server.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptionslibrary.exception.BadRequestException;
import ru.practicum.exceptionslibrary.model.ApiError;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final Exception e) {
        return new ApiError(HttpStatus.BAD_REQUEST, "The request was made incorrectly", e.toString());
    }
}
