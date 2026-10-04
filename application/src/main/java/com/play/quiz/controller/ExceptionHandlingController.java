package com.play.quiz.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.play.quiz.exception.EmailSendFailedException;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.util.ServerText;
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

    // "No question with id: 12" says something to a developer, who has it in the log; a player
    // only meets it on a stale link or a race, and gets the same sentence in their language.
    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class, RecordNotFoundException.class})
    public ResponseEntity<String> notFound(final RuntimeException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ServerText.t("err_not_found", "This could not be found. It may have been deleted."));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> inputOutputException(final IOException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ServerText.t("err_server", "Something went wrong on our side. Please try again."));
    }

    // Our own refusals are written for the player (and translated where thrown); Spring's
    // method security only ever says "Access Denied".
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied(final AccessDeniedException exception) {
        log.info(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access Denied".equals(exception.getMessage())
                        ? ServerText.t("err_access_denied", "You are not allowed to do that.")
                        : exception.getMessage());
    }

    // A failed password login. Same answer for an unknown email and a wrong password, so the
    // form does not tell anyone which addresses have accounts; the log says which it was. 400,
    // not 401: the site drops 401/403 silently when nobody is signed in, which at login is everyone.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationFailed(final AuthenticationException exception) {
        log.info("Login refused: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ServerText.t("err_bad_login", BAD_LOGIN));
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

    // Our own messages by field and constraint (err_email_email, err_crypto_address_pattern, ...);
    // without a row it is the validator's own message.
    private static String translated(final FieldError fieldError) {
        String field = fieldError.getField().replaceAll("\\[\\d+]", "").replace('.', '_');
        String key = ("err_" + field + "_" + fieldError.getCode()).toLowerCase(Locale.ROOT);
        return ServerText.t(key, fieldError.getDefaultMessage());
    }

    private static List<String> getViolatedFields(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()))
                .map(fieldError -> fieldError.getField() + " " + translated(fieldError))
                .toList();
    }
}
