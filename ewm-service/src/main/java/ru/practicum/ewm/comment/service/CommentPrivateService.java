package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.model.dto.CommentDto;
import ru.practicum.ewm.comment.model.dto.CommentShortDto;

public interface CommentPrivateService {

    CommentDto create(Long userId, Long eventId, CommentShortDto commentShortDto);

    CommentDto update(Long userId, Long eventId, CommentShortDto commentShortDto);

    void delete(Long userId, Long eventId);

    CommentDto getOne(Long userId, Long eventId);
}
