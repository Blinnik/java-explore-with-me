package ru.practicum.ewm.comment.service.mapper;

import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .commentator(comment.getCommentator().getId())
                .event(comment.getEvent().getId())
                .lastUpdated(comment.getLastUpdated())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
        System.out.println(commentDto);
        return commentDto;
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
