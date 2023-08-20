package ru.practicum.ewm.comment.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.comment.service.CommentAdminService;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CommentAdminServiceImpl implements CommentAdminService {
    CommentRepository commentRepository;

    @Override
    @Transactional
    public void delete(Long commId) {
        if (!commentRepository.existsById(commId)) {
            throw new NotFoundException(String.format("Comment with id=%s was not found", commId));
        }

        commentRepository.deleteById(commId);
        log.debug("A comment with an id {} was deleted", commId);
    }
}
