package org.springfeed.newsfeed.domain.user.dto.request;

import lombok.Getter;

// 비밀번호 변경 시 들어올 정보
@Getter
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;

}
