package ru.practicum.ewm.user.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.NewUserRequest;
import ru.practicum.ewm.user.model.dto.UserDto;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserAdminService;
import ru.practicum.ewm.user.service.mapper.UserMapper;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {
    UserRepository repository;

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        User user = UserMapper.toUser(userRequest);

        User createdUser = repository.save(user);
        log.debug("New user was created, id = {}", createdUser.getId());

        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public List<UserDto> getUsers(List<Long> userIds, PaginationConfig paginationConfig) {
        List<User> users;
        if (userIds == null) {
            users = repository.findAll(paginationConfig.getPageable()).getContent();
        } else {
            users = repository.findAllByIdIn(userIds, paginationConfig.getPageable());
        }
        log.debug("A list of users was received");

        return UserMapper.toUserDto(users);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        repository.deleteById(userId);
        log.debug("A user with an id {} was deleted", userId);
    }
}
