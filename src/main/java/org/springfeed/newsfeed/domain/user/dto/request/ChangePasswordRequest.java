package org.springfeed.newsfeed.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springfeed.newsfeed.global.annotation.ValidPasswordPattern;

// 비밀번호 변경 시 들어올 정보
@Getter
public class ChangePasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력하세요.")
    @ValidPasswordPattern
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력하세요.")
    @ValidPasswordPattern
    private String newPassword;

}
