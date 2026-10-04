package com.play.quiz.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.play.quiz.exception.EmailSendFailedException;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ExceptionHandlingController {

    public static final String BAD_LOGIN = "Wrong email or password";

    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> userNotFound(final RuntimeException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> recordNotFound(final RecordNotFoundException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> inputOutputException(final IOException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied(final AccessDeniedException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }

    // A failed password login. Same answer for an unknown email and a wrong password, so the
    // form does not tell anyone which addresses have accounts; the log says which it was. 400,
    // not 401: the site drops 401/403 silently when nobody is signed in, which at login is everyone.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationFailed(final AuthenticationException exception) {
        log.info("Login refused: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BAD_LOGIN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgument(final IllegalArgumentException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler({EmailSendFailedException.class, RuntimeException.class})
    public ResponseEntity<String> runtimeException(final RuntimeException exception) {
        // The class name too: a wrapped persistence error carries a message that says nothing
        // about what threw it.
        log.error("Unhandled {}: {}", exception.getClass().getName(), exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final List<String> violatedFields = getViolatedFields(exception.getBindingResult().getFieldErrors());
        log.info("Validation failed: {}", violatedFields);
        return ResponseEntity.badRequest()
                .body(violatedFields);
    }

    private static List<String> getViolatedFields(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList();
    }
}
