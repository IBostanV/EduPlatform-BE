package com.play.quiz.service;

import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.ClientError;
import com.play.quiz.record.ClientErrorEntry;
import com.play.quiz.record.ClientErrorInput;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.ClientErrorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Errors from players' browsers: sent by anyone, read and cleared by admins.
 *
 * ponytail: kept until an admin deletes them; add a scheduled purge of old rows if clearing by
 * hand stops keeping up.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ClientErrorService {

    private final ClientErrorRepository clientErrorRepository;

    // The player (none for a guest) and the time are filled in by the audit aspect on save.
    @Transactional
    public void save(final ClientErrorInput input) {
        ClientError saved = clientErrorRepository.save(ClientError.builder()
                .kind(input.kind().trim())
                .message(input.message().trim())
                .stack(StringUtils.hasText(input.stack()) ? input.stack() : null)
                .page(StringUtils.hasText(input.page()) ? input.page().trim() : null)
                .userAgent(StringUtils.hasText(input.userAgent()) ? input.userAgent().trim() : null)
                .build());
        log.info("Saved client error id: {}, kind: {}", saved.getClientErrorId(), saved.getKind());
    }

    @Transactional(readOnly = true)
    public List<ClientErrorEntry> getLatest() {
        return clientErrorRepository.findTop200ByOrderByCreatedDateDesc().stream()
                .map(ClientErrorService::toEntry)
                .toList();
    }

    public long count() {
        return clientErrorRepository.count();
    }

    @Transactional
    public void delete(final Long clientErrorId) {
        clientErrorRepository.deleteById(clientErrorId);
        log.info("Deleted client error id: {}", clientErrorId);
    }

    @Transactional
    public void deleteAll() {
        clientErrorRepository.deleteAllInBatch();
        log.info("Deleted all client errors");
    }

    private static ClientErrorEntry toEntry(final ClientError error) {
        return new ClientErrorEntry(
                error.getClientErrorId(),
                error.getKind(),
                error.getMessage(),
                error.getStack(),
                error.getPage(),
                error.getUserAgent(),
                Optional.ofNullable(error.getCreatedBy()).map(UserSummary::of).orElse(null),
                error.getCreatedDate());
    }
}
