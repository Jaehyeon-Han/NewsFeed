package org.springfeed.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 게시글 작성 시 들어올 정보
@Getter
public class CreatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
