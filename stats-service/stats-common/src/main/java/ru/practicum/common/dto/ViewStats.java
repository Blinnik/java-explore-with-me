package ru.practicum.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStats {
    String app;

    String uri;

    Long hits;

    // JPQL does not see lombok constructor
    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}




