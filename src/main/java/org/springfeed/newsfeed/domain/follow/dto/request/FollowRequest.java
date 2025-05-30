package org.springfeed.newsfeed.domain.follow.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FollowRequest {

    @NotNull(message = "followingId는 필수입니다.")
    private Long followingId;

}
