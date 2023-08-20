package ru.practicum.ewm.comment.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.dto.CommentDto;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.CommentPublicService;
import ru.practicum.ewm.comment.service.mapper.CommentMapper;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CommentPublicServiceImpl implements CommentPublicService {
    CommentRepository commentRepository;
    EventRepository eventRepository;

    @Override
    public List<CommentDto> getAll(Long eventId, PaginationConfig paginationConfig) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id=%s was not found", eventId));
        }

        List<Comment> comments = commentRepository.findAllByEventId(eventId, paginationConfig.getPageable());

        log.debug("Comments of the event with id={} were received", eventId);

        return CommentMapper.toCommentDto(comments);
    }

    @Override
    public CommentDto getOne(Long commId) {
        Comment comment = commentRepository.findById(commId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%s was not found", commId)));

        log.debug("A comment with id {} was received", commId);

        return CommentMapper.toCommentDto(comment);
    }
}
