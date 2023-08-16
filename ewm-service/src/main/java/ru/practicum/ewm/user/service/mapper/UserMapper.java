package ru.practicum.ewm.user.service.mapper;

import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.NewUserRequest;
import ru.practicum.ewm.user.model.dto.UserDto;
import ru.practicum.ewm.user.model.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(NewUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> toUserDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static UserShortDto toUserShortDto(User initiator) {
        return UserShortDto.builder()
                .id(initiator.getId())
                .name(initiator.getName())
                .build();
    }
}
