package ru.practicum.ewm.common.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeFormatter {
    static String pattern = "yyyy-MM-dd HH:mm:ss";
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
