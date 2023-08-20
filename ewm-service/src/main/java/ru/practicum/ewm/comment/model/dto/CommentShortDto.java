package ru.practicum.ewm.comment.model.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentShortDto {
    @NotBlank(message = "text cannot be blank")
    @Length(message = "text: the string length should be from 5 to 2000 characters", min = 5, max = 2000)
    String text;
}
