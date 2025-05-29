package org.springfeed.newsfeed.domain.comment.repository;

import org.springfeed.newsfeed.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository <Comment, Long> {
}
