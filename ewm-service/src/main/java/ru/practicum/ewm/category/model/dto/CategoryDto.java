package ru.practicum.ewm.category.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.common.validation.constraint.NullOrNotBlank;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long id;

    @NullOrNotBlank(message = "name must not be blank")
    @Length(message = "name: the string length should be from 1 to 50 characters", min = 1, max = 50)
    String name;
}
