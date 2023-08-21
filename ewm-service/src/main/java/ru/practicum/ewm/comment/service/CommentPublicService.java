package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.model.dto.CommentDto;
import ru.practicum.ewm.common.param.PaginationConfig;

import java.util.List;

public interface CommentPublicService {

    List<CommentDto> getAll(Long eventId, PaginationConfig paginationConfig);

    CommentDto getOne(Long commId);
}
