package ru.practicum.ewm.user.service;

import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.user.model.dto.NewUserRequest;
import ru.practicum.ewm.user.model.dto.UserDto;

import java.util.List;

public interface UserAdminService {
    UserDto createUser(NewUserRequest userRequest);

    List<UserDto> getUsers(List<Long> userIds, PaginationConfig paginationConfig);

    void deleteUser(Long userId);
}
