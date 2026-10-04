package com.play.quiz.announcement;

import static com.play.quiz.controller.RestEndpoint.CONTEXT_PATH;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_ANNOUNCEMENT;
import static com.play.quiz.controller.RestEndpoint.WS_BROKER_PARTY;

import java.time.LocalDateTime;
import java.util.List;

import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Sent ones are pushed over the socket (TOPIC); every player reads their unseen announcements and dismisses them; listing, sending and deleting
// them is admin-only (WebSecurity).
@Log4j2
@RestController
@RequestMapping(CONTEXT_PATH + REQUEST_MAPPING_ANNOUNCEMENT)
@RequiredArgsConstructor
public class AnnouncementController {

    /** Every signed-in player's socket listens here; the page shows what arrives at once. */
    public static final String TOPIC = WS_BROKER_PARTY + "/announcement";

    public record AnnouncementInput(@NotBlank @Size(max = 200) String title, @Size(max = 4000) String content) {}

    private final AnnouncementRepository announcementRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final SimpMessagingTemplate messaging;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Announcement>> all() {
        return ResponseEntity.ok(announcementRepository.findAllByOrderByAnnouncementIdDesc());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Announcement> send(@Valid @RequestBody final AnnouncementInput input) {
        Announcement saved = announcementRepository.save(Announcement.builder()
                .title(input.title().strip())
                .content(input.content() == null ? null : input.content().strip())
                .createdBy(currentAccountId())
                .createdDate(LocalDateTime.now())
                .build());
        log.info("Announcement {} sent by account {}", saved.getAnnouncementId(), saved.getCreatedBy());
        // Not offered back to the admin who wrote it; the page skips the push for them as well.
        announcementRepository.markSeen(saved.getCreatedBy(), saved.getAnnouncementId());
        // One frame per open connection, once: no polling. A player offline now gets it from
        // /unseen when their socket (re)connects.
        messaging.convertAndSend(TOPIC, saved);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{announcementId}")
    public ResponseEntity<Void> delete(@PathVariable final Long announcementId) {
        announcementRepository.deleteById(announcementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/unseen", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Announcement>> unseen() {
        return ResponseEntity.ok(announcementRepository.findUnseen(currentAccountId()));
    }

    /** Dismisses this announcement and every older one. */
    @PostMapping("/{announcementId}/seen")
    public ResponseEntity<Void> seen(@PathVariable final Long announcementId) {
        announcementRepository.markSeen(currentAccountId(), announcementId);
        return ResponseEntity.noContent().build();
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
