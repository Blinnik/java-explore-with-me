package ru.practicum.ewm.compilation.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    List<Long> events;

    @NotBlank(message = "title cannot be blank")
    @Length(message = "title: the string length should be from 1 to 50 characters", min = 1, max = 50)
    String title;

    boolean pinned;
}
