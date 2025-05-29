package org.springfeed.newsfeed.domain.follow.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class FollowingListResponse {
    private int totalFollowingCount;

    private List<FollowUserResponse> followings; // 팔로잉한 사용자 리스트

    public FollowingListResponse(List<FollowUserResponse> followings) {
        this.totalFollowingCount = followings.size();
        this.followings = followings;
    }
}
