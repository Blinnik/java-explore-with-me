package ru.practicum.ewm.comment.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.dto.CommentDto;
import ru.practicum.ewm.comment.model.dto.CommentShortDto;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.CommentPrivateService;
import ru.practicum.ewm.comment.service.mapper.CommentMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.exceptionslibrary.exception.ConflictException;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CommentPrivateServiceImpl implements CommentPrivateService {
    CommentRepository commentRepository;
    EventRepository eventRepository;
    UserRepository userRepository;
    RequestRepository requestRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, CommentShortDto commentShortDto) {
        User commentator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%s was not found", userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId)));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("The user cannot write a comment to an unpublished event");
        }

        if (commentRepository.existsByCommentatorIdAndEventId(userId, eventId)) {
            throw new ConflictException("The user cannot leave multiple comments on the same event");
        }

        if (!requestRepository.existsByRequesterIdAndEventIdAndStatusEquals(userId, eventId, RequestStatus.CONFIRMED)) {
            throw new ConflictException("The user did not send a request to participate in the event, " +
                    "or it was not confirmed");
        }

        Comment comment = Comment.builder()
                .text(commentShortDto.getText())
                .commentator(commentator)
                .event(event)
                .created(LocalDateTime.now())
                .build();

        Comment createdComment = commentRepository.save(comment);
        log.debug("New comment was created, id = {}", createdComment.getId());

        return CommentMapper.toCommentDto(createdComment);
    }

    @Override
    public CommentDto update(Long userId, Long eventId, CommentShortDto commentShortDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id=%s was not found", eventId));
        }

        Comment comment = commentRepository.findByCommentatorIdAndEventId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("The user did not leave a comment on this event"));

        comment.setText(commentShortDto.getText());
        comment.setLastUpdated(LocalDateTime.now());
        commentRepository.save(comment);

        log.debug("A comment with an id {} was updated", comment.getId());

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void delete(Long userId, Long eventId) {
        Comment comment = commentRepository.findByCommentatorIdAndEventId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("The user did not leave a comment on this event"));

        Long commId = comment.getId();

        commentRepository.deleteById(commId);
        log.debug("A comment with an id {} was deleted", commId);
    }

    @Override
    public CommentDto getOne(Long userId, Long eventId) {
        Comment comment = commentRepository.findByCommentatorIdAndEventId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("The user did not leave a comment on this event"));

        log.debug("A comment with id {} was received", comment.getId());

        return CommentMapper.toCommentDto(comment);
    }
}
