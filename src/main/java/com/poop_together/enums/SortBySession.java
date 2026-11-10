package com.poop_together.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public enum SortBySession {
    STARTED_AT("started_at", "startedAt"),
    UPDATED_AT("updated_at", "updatedAt"),
    DURATION_SECONDS("duration_seconds", "durationSeconds");

    String value;
    String property;

    SortBySession(String value, String property) {
        this.value = value;
        this.property = property;
    }
}
