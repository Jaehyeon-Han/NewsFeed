package org.springfeed.newsfeed.domain.follow.dto.response;

import lombok.Getter;
import org.springfeed.newsfeed.domain.entity.User;

@Getter
public class FollowUserResponse {
    private Long id;
    private String nickname;
    private String introduction;

    public FollowUserResponse(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
    }
}
