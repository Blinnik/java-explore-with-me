package ru.practicum.ewm.comment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.model.dto.CommentDto;
import ru.practicum.ewm.comment.model.dto.CommentShortDto;
import ru.practicum.ewm.comment.service.CommentPrivateService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentPrivateController {
    CommentPrivateService commentPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @RequestBody @Valid CommentShortDto commentShortDto) {

        return commentPrivateService.create(userId, eventId, commentShortDto);
    }

    @PatchMapping
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @RequestBody @Valid CommentShortDto commentShortDto) {

        return commentPrivateService.update(userId, eventId, commentShortDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long eventId) {
        commentPrivateService.delete(userId, eventId);
    }

    @GetMapping
    public CommentDto getOne(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        return commentPrivateService.getOne(userId, eventId);
    }
}
