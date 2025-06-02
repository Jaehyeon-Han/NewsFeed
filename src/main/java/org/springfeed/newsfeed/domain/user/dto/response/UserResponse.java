package org.springfeed.newsfeed.domain.user.dto.response;

// 사용자 관련 응답 객체

import lombok.Getter;
import org.springfeed.newsfeed.domain.entity.User;

@Getter
public class UserResponse {

    private final String email;
    private final String nickname;
    private final String introduction;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
    }
}
