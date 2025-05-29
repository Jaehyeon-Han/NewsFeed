package org.springfeed.newsfeed.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.Follow;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowerListResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowingListResponse;
import org.springfeed.newsfeed.domain.follow.repository.FollowRepository;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowUserResponse;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowResponse followUser(Long currentUserId, Long followingId) {

        User follower = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("로그인한 사용자를 찾을 수 없습니다."));

        User following = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("팔로우할 사용자를 찾을 수 없습니다."));

        if (currentUserId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다.");
        }

        if (followRepository.existsByFollowerIdAndFollowingId(currentUserId, followingId)){
            throw new IllegalArgumentException("이미 팔로우를 했습니다.");
        }

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        return new FollowResponse(follow);
    }

    public FollowingListResponse getFollowings(Long userId) {
        List<Follow> followingsByUserId = followRepository.findByFollowerId(userId);
        List<FollowUserResponse> followings = new ArrayList<>();

        for (Follow follow : followingsByUserId) {
            User followingUser = follow.getFollowing(); // 내가 팔로우한 사용자
            FollowUserResponse response = new FollowUserResponse(followingUser);
            followings.add(response);
        }

        return new FollowingListResponse(followings);
    }

    public FollowerListResponse getFollowers(Long userId) {
        List<Follow> followersByUserId = followRepository.findByFollowingId(userId);
        List<FollowUserResponse> followers = new ArrayList<>();

        for (Follow follow : followersByUserId) {
            User followerUser = follow.getFollower();  // 팔로우를 건 사용자
            FollowUserResponse response = new FollowUserResponse(followerUser);
            followers.add(response);
        }

        return new FollowerListResponse(followers);
    }

    @Transactional
    public void unfollowUser(Long currentUserId, Long followingId) {

        Follow follow = followRepository.findByFollowerIdAndFollowingId(currentUserId, followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우한 내역이 없습니다."));

        followRepository.delete(follow);
    }

}
