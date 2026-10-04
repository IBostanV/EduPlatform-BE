package com.play.quiz.coin;

import java.util.List;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Spending coins. The balance itself comes with the account (/api/user/get-current-user).
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_COIN)
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @PostMapping(value = "/streak-freeze", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoinService.Purchase> buyStreakFreeze() {
        return ResponseEntity.ok(coinService.buyStreakFreeze());
    }

    /** The body is the termIds of the options on screen. */
    @PostMapping(value = "/hint/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoinService.Purchase> buyHint(@PathVariable final Long questionId,
                                                        @RequestBody final List<Long> shownTermIds) {
        return ResponseEntity.ok(coinService.buyHint(questionId, shownTermIds));
    }

    @PostMapping(value = "/extra-time", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoinService.Purchase> buyExtraTime() {
        return ResponseEntity.ok(coinService.buyExtraTime());
    }
}
