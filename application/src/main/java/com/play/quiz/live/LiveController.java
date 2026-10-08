package com.play.quiz.live;

import com.play.quiz.controller.RestEndpoint;
import com.play.quiz.dto.AnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Live matches. The actions come in over plain requests, which carry the player's session like
 * everything else; what happens next goes out over the socket to everyone in the room
 * (/user/live), so a browser never has to ask how the match stands.
 */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_LIVE)
@RequiredArgsConstructor
public class LiveController {

    private final LiveService liveService;

    public record AnswerInput(int index, AnswerDto answer) {}

    public record FlipInput(int card) {}

    @PostMapping(value = "/rooms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> create(@RequestBody final LiveService.CreateInput input) {
        return ResponseEntity.ok(liveService.create(input));
    }

    @GetMapping(value = "/rooms/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> get(@PathVariable final String code) {
        return ResponseEntity.ok(liveService.get(code));
    }

    @PostMapping(value = "/rooms/{code}/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> join(@PathVariable final String code) {
        return ResponseEntity.ok(liveService.join(code));
    }

    @PostMapping("/rooms/{code}/decline")
    public ResponseEntity<Void> decline(@PathVariable final String code) {
        liveService.decline(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{code}/leave")
    public ResponseEntity<Void> leave(@PathVariable final String code) {
        liveService.leave(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/rooms/{code}/again", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> again(@PathVariable final String code) {
        return ResponseEntity.ok(liveService.again(code));
    }

    @PostMapping(value = "/rooms/{code}/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> start(@PathVariable final String code) {
        return ResponseEntity.ok(liveService.start(code));
    }

    @PostMapping(value = "/rooms/{code}/answer", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> answer(@PathVariable final String code,
                                                       @RequestBody final AnswerInput input) {
        return ResponseEntity.ok(liveService.answer(code, input.index(), input.answer()));
    }

    @PostMapping(value = "/rooms/{code}/flip", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiveService.RoomView> flip(@PathVariable final String code, @RequestBody final FlipInput input) {
        return ResponseEntity.ok(liveService.flip(code, input.card()));
    }
}
