package com.play.quiz.record;

import java.util.Set;

public record CreateGroupInput(String name, Set<Long> participantIds) {}
