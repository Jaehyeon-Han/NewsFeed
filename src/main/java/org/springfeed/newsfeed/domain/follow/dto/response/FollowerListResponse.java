package org.springfeed.newsfeed.domain.follow.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class FollowerListResponse {
    private int totalFollowerCount;
    private List<FollowUserResponse> followers;

    public FollowerListResponse(List<FollowUserResponse> followers) {
        this.totalFollowerCount = followers.size();
        this.followers = followers;
    }
}
