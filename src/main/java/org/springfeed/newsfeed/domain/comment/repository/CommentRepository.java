package org.springfeed.newsfeed.domain.comment.repository;

import org.springfeed.newsfeed.domain.entity.Comment;
import org.springfeed.newsfeed.global.error.exception.CommentNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findAllByPostId(Long postId);

    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(CommentNotFoundException::new);
    }
}
