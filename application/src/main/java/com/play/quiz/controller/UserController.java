package com.play.quiz.controller;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.dto.UserOccupationDto;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.record.PasswordInput;
import com.play.quiz.service.UserOccupationService;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_USER;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final UserOccupationService userOccupationService;

    @GetMapping(value = "/get-current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getUser(Principal principal) {
        Account account = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountDto>> getAccountList() {
        return ResponseEntity.ok(userService.getAccountList());
    }

    @GetMapping(value = "/occupations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserOccupationDto>> getOccupationList() {
        return ResponseEntity.ok(userOccupationService.getAllOccupations());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AccountDto> saveUserProfileInfo(@RequestPart(name = "request") final AccountDto accountDto,
                                                          @RequestPart(required = false) final MultipartFile avatar) {
        Account account = userService.save(accountDto, avatar);
        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @PostMapping(value = "/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody PasswordInput password) {
        return ResponseEntity.ok(userService.verifyOldPassword(password));
    }

    @PostMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody PasswordInput password) {
        userService.changePassword(password);
    }

    @PostMapping(value = "/change-language")
    public ResponseEntity<Boolean> changeLanguage(@RequestBody Language language) {
        return ResponseEntity.ok(userService.changeLanguage(language));
    }
}
