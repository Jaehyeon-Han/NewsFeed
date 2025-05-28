package org.springfeed.newsfeed.global.auth.dto.response;

public class UserResponse {
    private Long id;
    private String nickname;

    public UserResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
