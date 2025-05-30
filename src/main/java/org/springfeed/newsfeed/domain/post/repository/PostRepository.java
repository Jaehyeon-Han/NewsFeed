package org.springfeed.newsfeed.domain.post.repository;

import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.global.error.exception.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findPostByIdOrElseThrow(Long postId) {
        return findById(postId).orElseThrow(PostNotFoundException::new);
    }

    Page<Post> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        WHERE p.author.id IN (
            SELECT f.following.id FROM Follow f
            WHERE f.follower.id = :currentId
        )
    """)
    Page<Post> findPostsByFollowings(@Param("currentId") Long currentId, Pageable pageable);

}
