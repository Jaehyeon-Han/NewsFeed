package org.springfeed.newsfeed.domain.follow.dto.response;

import lombok.Getter;
import org.springfeed.newsfeed.domain.entity.Follow;

import java.time.LocalDateTime;

@Getter
public class FollowResponse {

    private final Long followId;

    private final Long followerId; // 팔로우 건 사용자 ID
    private final String followerNickname; // 팔로우 건 사용자 닉네임

    private final Long followingId; // 팔로우 당한 사용자 ID
    private final String followingNickname; // 팔로우 당한 사용자 닉네임

    private final LocalDateTime followedAt; // 팔로우가 생성된 시각

    public FollowResponse(Follow follow) {
        this.followId = follow.getId();
        this.followerId = follow.getFollower().getId();
        this.followerNickname = follow.getFollower().getNickname();
        this.followingId = follow.getFollowing().getId();
        this.followingNickname = follow.getFollowing().getNickname();
        this.followedAt = follow.getStartedFollowingAt();
    }
}