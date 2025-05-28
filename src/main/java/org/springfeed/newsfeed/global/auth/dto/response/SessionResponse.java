package org.springfeed.newsfeed.global.auth.dto.response;

import lombok.Getter;

@Getter
public class SessionResponse {
    private Long id;
    private String nickname;

    public SessionResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
