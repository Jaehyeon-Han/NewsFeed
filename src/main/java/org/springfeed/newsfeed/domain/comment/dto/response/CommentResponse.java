package org.springfeed.newsfeed.domain.comment.dto.response;

import lombok.Getter;

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

    public CommentResponse(Long id,
        Long postId,
        Long userId,
        String nickname,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
    ) {
        this.id = id;
        this.postId = postId;
        this.authorId = userId;
        this.author = nickname;
        this.comment = comment;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
