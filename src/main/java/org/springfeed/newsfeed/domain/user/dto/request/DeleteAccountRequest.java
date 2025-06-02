package org.springfeed.newsfeed.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springfeed.newsfeed.global.annotation.ValidPasswordPattern;

// 회원 탈퇴 시 들어올 정보
@Getter
public class DeleteAccountRequest {

    @NotBlank(message = "비밀번호를 입력하세요.")
    @ValidPasswordPattern
    private String password;
}
