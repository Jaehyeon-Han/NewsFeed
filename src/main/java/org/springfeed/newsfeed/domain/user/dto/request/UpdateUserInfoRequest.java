package org.springfeed.newsfeed.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserInfoRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    private String nickname;

    @Size(max = 500, message = "소개글은 500자 이하로 입력해주세요.")  //디폴트값 설정
    private String introduction;

}
