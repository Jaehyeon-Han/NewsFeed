package org.springfeed.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

// 게시글 작성 시 들어올 정보
@Getter
public class CreatePostRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 최대 50 글자입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 500, message = "내용은 최대 500 글자입니다.")
    private String content;
}
