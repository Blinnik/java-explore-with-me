package ru.practicum.ewm.comment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.model.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentPublicService;
import ru.practicum.ewm.common.param.PaginationConfig;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentPublicController {
    CommentPublicService commentPublicService;

    @GetMapping
    public List<CommentDto> getAll(@RequestParam Long eventId,
                                   PaginationConfig paginationConfig) {
        return commentPublicService.getAll(eventId, paginationConfig);
    }

    @GetMapping("/{commId}")
    public CommentDto getOne(@PathVariable Long commId) {
        return commentPublicService.getOne(commId);
    }
}
