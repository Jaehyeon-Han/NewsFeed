package org.springfeed.newsfeed.domain.post.repository;

import org.springfeed.newsfeed.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

// Todo
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findPostByIdOrElseThrow(Long postId) {
        return findById(postId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
