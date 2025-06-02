package org.springfeed.newsfeed.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// 게시글 응답
@Getter
@Builder
public class PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final Long authorId;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    public PostResponse(Long id,
        String title,
        String content,
        Long authorId,
        String author,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
    ) {
        this.postId = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.author = author;
        this.createdAt = createdAt;
        this.lastModifiedAt = modifiedAt;
    }

}
