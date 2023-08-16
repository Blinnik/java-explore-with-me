package ru.practicum.ewm.compilation.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.common.validation.constraint.NullOrNotBlank;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    List<Long> events;

    @NullOrNotBlank(message = "title must not be blank")
    @Length(message = "title: the string length should be from 1 to 50 characters", min = 1, max = 50)
    String title;

    Boolean pinned;
}
