package ru.practicum.ewm.category.model.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class NewCategoryDto {
    @NotBlank(message = "name cannot be blank")
    @Length(message = "name: the string length should be from 1 to 50 characters", min = 1, max = 50)
    private String name;
}
