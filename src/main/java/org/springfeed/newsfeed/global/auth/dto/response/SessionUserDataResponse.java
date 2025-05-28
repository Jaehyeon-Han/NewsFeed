package org.springfeed.newsfeed.global.auth.dto.response;

import lombok.Getter;

@Getter
public class SessionUserDataResponse {
    private Long id;
    private String nickname;

    public SessionUserDataResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
