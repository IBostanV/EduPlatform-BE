package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CLIENT_ERROR;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import com.play.quiz.record.ClientErrorEntry;
import com.play.quiz.record.ClientErrorInput;
import com.play.quiz.security.FeedbackRateLimiter;
import com.play.quiz.service.ClientErrorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Errors from players' browsers. Sending is open to guests and free of CSRF (WebSecurity): all it
// can do is add a capped number of rows. Reading and clearing them is admin-only.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_CLIENT_ERROR)
@RequiredArgsConstructor
public class ClientErrorController {

    private final ClientErrorService clientErrorService;

    // A page stuck in an error loop reports the same thing over and over; past this it is dropped.
    private final FeedbackRateLimiter rateLimiter = new FeedbackRateLimiter(30, Duration.ofMinutes(10));

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> report(@Valid @RequestBody final ClientErrorInput input,
                                       final Principal principal,
                                       final HttpServletRequest request) {
        String sender = Objects.nonNull(principal) ? principal.getName() : request.getRemoteAddr();
        if (rateLimiter.tryAcquire(sender)) {
            clientErrorService.save(input);
        }
        // Accepted either way: the browser does nothing with the answer.
        return ResponseEntity.accepted().build();
    }

    // The newest 200, newest first.
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientErrorEntry>> getLatest() {
        return ResponseEntity.ok(clientErrorService.getLatest());
    }

    // Every stored one, past the 200 shown too: the badge on the dashboard entry.
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(clientErrorService.count());
    }

    @DeleteMapping("/{clientErrorId}")
    public ResponseEntity<Void> delete(@PathVariable final Long clientErrorId) {
        clientErrorService.delete(clientErrorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        clientErrorService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
