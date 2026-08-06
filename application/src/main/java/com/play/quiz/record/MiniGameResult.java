package com.play.quiz.record;

// Home page mini game: whether the pick was right, and the right option (by termId for glossary
// answers, by id otherwise) so the page can highlight it.
public record MiniGameResult(boolean correct, Long answerId, Long termId, String content) {
}
