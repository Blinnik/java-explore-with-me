package ru.practicum.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class EndpointHit {
    @NotNull(message = "app cannot be null")
    @Length(message = "app: the string length should be from 1 to 40 characters", min = 1, max = 40)
    String app;

    @NotNull(message = "uri cannot be null")
    @Length(message = "uri: the string length should be from 2 to 300 characters", min = 2, max = 300)
    String uri;

    @NotNull(message = "ip cannot be null")
    @Pattern(message = "ip must match the format X.X.X.X",
            regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    String ip;

    @Length(message = "timestamp: string length should be up to 19 characters", max = 19)
    String timestamp;
}
