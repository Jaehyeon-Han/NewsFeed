package org.springfeed.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 게시글 수정 시 들어올 정보
@Getter
public class UpdatePostRequest {
    // Todo: 빈 검증?
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
