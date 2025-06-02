package org.springfeed.newsfeed.domain.comment.dto.response;

import lombok.Getter;
import org.springfeed.newsfeed.domain.entity.Comment;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private final Long id;
    private final Long postId;
    private final Long authorId;
    private final String author;
    private final String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    public CommentResponse(Comment comment) {
        User author = comment.getAuthor();
        Post post = comment.getPost();

        this.id = comment.getId();
        this.postId = post.getId();
        this.authorId = author.getId();
        this.author = author.getNickname();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.lastModifiedAt = comment.getLastModifiedAt();
    }
}
