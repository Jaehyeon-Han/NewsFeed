package org.springfeed.newsfeed.domain.post.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

// 게시글 응답
@Getter
public class PostResponse {
    // Todo

    private final Long id;
    private final String title;
    private final String contents;
    private final Long authorId;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public PostResponse(Long id, String title, String contents, Long authorId, String author, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.authorId = authorId;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
