package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentatorIdAndEventId(Long commentatorId, Long eventId);

    boolean existsByCommentatorIdAndEventId(Long commentatorId, Long eventId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);
}
