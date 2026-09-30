package com.play.quiz.enums;

// What a message pushed over the socket means for the chat on screen: a new one to append,
// an existing one whose content changed, or one to take away.
public enum MessageEvent {
    CREATED,
    EDITED,
    DELETED
}
