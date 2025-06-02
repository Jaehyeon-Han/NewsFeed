package org.springfeed.newsfeed.domain.post.dto.response;

import lombok.Getter;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;

import java.time.LocalDateTime;

// 게시글 응답
@Getter
public class PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final Long authorId;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    public PostResponse(Post post){
        User author = post.getAuthor();

        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = author.getId();
        this.author = author.getNickname();
        this.createdAt = post.getCreatedAt();
        this.lastModifiedAt = post.getLastModifiedAt();
    }

}
