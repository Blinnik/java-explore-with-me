package ru.practicum.ewm.common.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptionslibrary.exception.BadRequestException;
import ru.practicum.exceptionslibrary.exception.ConflictException;
import ru.practicum.exceptionslibrary.exception.ForbiddenException;
import ru.practicum.exceptionslibrary.exception.NotFoundException;
import ru.practicum.exceptionslibrary.model.ApiError;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({BadRequestException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final Exception e) {
        return new ApiError(HttpStatus.BAD_REQUEST, "The request was made incorrectly", e.toString());
    }

    @ExceptionHandler({ConflictException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final RuntimeException e) {
        if (e.getClass().equals(ConstraintViolationException.class)) {
            return new ApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.", e.toString());
        }

        return new ApiError(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.",
                e.toString());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final RuntimeException e) {
        return new ApiError(HttpStatus.NOT_FOUND,
                "The required object was not found.",
                e.toString());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final RuntimeException e) {
        return new ApiError(HttpStatus.FORBIDDEN,
                "For the requested operation the conditions are not met.",
                e.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerErrorException(final Throwable e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e.toString());
    }
}