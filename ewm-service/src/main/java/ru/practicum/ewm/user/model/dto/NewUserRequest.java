package ru.practicum.ewm.user.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @NotBlank(message = "name cannot be blank")
    @Length(message = "name: the string length should be from 2 to 250 characters", min = 2, max = 250)
    String name;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "email does not match the standard format")
    @Length(message = "email: the string length should be from 6 to 254 characters", min = 6, max = 254)
    String email;
}
