package com.play.quiz.feed;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.domain.Category;
import com.play.quiz.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Headlines from the world about the categories this site has, from the GDELT project's free
 * article search (no key, no sign-up): api.gdeltproject.org/api/v2/doc/doc.
 *
 * <p>Fetched once a day, in the background: GDELT asks for no more than one request every five
 * seconds, and there is one per category, so a reader never waits on it. Until the day's fetch
 * lands, readers get yesterday's headlines (or none, on a fresh start). A failed fetch keeps what
 * there was and is tried again after {@link #RETRY_AFTER}.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class WorldNews {

    private static final String SEARCH = "https://api.gdeltproject.org/api/v2/doc/doc"
            + "?mode=artlist&format=json&sort=datedesc&timespan=3d&maxrecords=%d&query=%s";
    // GDELT matches anywhere in the article, so most hits only mention the word in passing. Only
    // headlines that name the category are kept, which takes asking for more than are shown.
    private static final int SEARCHED = 25;
    private static final int PER_CATEGORY = 3;
    private static final Duration SPACING = Duration.ofSeconds(10);
    private static final Duration RETRY_AFTER = Duration.ofHours(1);
    private static final DateTimeFormatter SEEN_DATE = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    @Qualifier("taskExecutor")
    private final Executor taskExecutor;

    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final AtomicBoolean fetching = new AtomicBoolean();
    private volatile List<FeedItem> headlines = List.of();
    private volatile LocalDate fetchedOn;
    private volatile LocalDateTime lastTry = LocalDateTime.MIN;

    /** The latest headlines; starts the day's fetch when there has not been one yet. */
    public List<FeedItem> headlines() {
        boolean stale = !LocalDate.now().equals(fetchedOn) && lastTry.isBefore(LocalDateTime.now().minus(RETRY_AFTER));
        if (stale && fetching.compareAndSet(false, true)) {
            lastTry = LocalDateTime.now();
            taskExecutor.execute(this::fetch);
        }
        return headlines;
    }

    // ponytail: one request per category, 10s apart (GDELT turns away faster), so 50 categories take
    // eight minutes once a day;
    // group several names into one OR query if the list grows past that.
    private void fetch() {
        try {
            List<Category> categories = categoryRepository.findAllActive(Sort.by("name"));
            List<FeedItem> found = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            boolean anyAnswered = false;
            int failed = 0;

            for (Category category : categories) {
                String name = category.getName();
                if (name == null || name.isBlank() || name.length() < 3) {
                    continue;
                }
                try {
                    int kept = 0;
                    for (JsonNode article : search(name)) {
                        String url = article.path("url").asText();
                        boolean aboutIt = article.path("title").asText().toLowerCase(Locale.ROOT)
                                .contains(name.toLowerCase(Locale.ROOT));
                        if (kept < PER_CATEGORY && aboutIt && !url.isBlank() && seen.add(url)) {
                            found.add(toItem(article, url, name));
                            kept++;
                        }
                    }
                    anyAnswered = true;
                } catch (Exception exception) {
                    log.warn("World news for {} failed: {}", name, exception.getMessage());
                    failed++;
                }
                Thread.sleep(SPACING.toMillis());
            }

            // Every request failing is GDELT being down, not a quiet day: keep what there was.
            if (anyAnswered) {
                headlines = List.copyOf(found);
                fetchedOn = LocalDate.now();
                log.info("World news fetched: {} headlines from {} categories, {} failed",
                        found.size(), categories.size(), failed);
            } else {
                log.warn("World news: all {} searches failed, keeping {} old headlines; retry after {}",
                        failed, headlines.size(), RETRY_AFTER);
            }
        } catch (InterruptedException exception) {
            log.warn("World news fetch interrupted");
            Thread.currentThread().interrupt();
        } catch (RuntimeException exception) {
            log.warn("World news fetch failed", exception);
        } finally {
            fetching.set(false);
        }
    }

    private JsonNode search(final String name) throws Exception {
        String query = URLEncoder.encode("\"" + name.replace("\"", "") + "\" sourcelang:english", StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder(URI.create(SEARCH.formatted(SEARCHED, query)))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
        log.debug("World news search for {}", name);
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("HTTP " + response.statusCode());
        }
        // Being asked to slow down, or a query it does not like, comes back as a 200 with a line of
        // plain text. That is a failure, not a day without news.
        String body = response.body().trim();
        if (!body.startsWith("{")) {
            throw new IllegalStateException(body.substring(0, Math.min(120, body.length())));
        }
        return objectMapper.readTree(body).path("articles");
    }

    private static FeedItem toItem(final JsonNode article, final String url, final String category) {
        return FeedItem.builder()
                .key("WORLD-" + url)
                .type(FeedItem.Type.WORLD)
                .at(seenAt(article.path("seendate").asText()))
                .title(article.path("title").asText())
                .text(article.path("domain").asText())
                .url(url)
                .name(category)
                .build();
    }

    // GDELT's times are UTC; everything else in the feed is on this server's clock.
    private static LocalDateTime seenAt(final String seenDate) {
        try {
            return LocalDateTime.parse(seenDate, SEEN_DATE).atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        } catch (RuntimeException exception) {
            log.debug("World news seen date '{}' unreadable, using now", seenDate);
            return LocalDateTime.now();
        }
    }
}
