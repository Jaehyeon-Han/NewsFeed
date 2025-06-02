package org.springfeed.newsfeed.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.Follow;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowUserResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowerListResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowingListResponse;
import org.springfeed.newsfeed.domain.follow.repository.FollowRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.error.exception.AlreadyFollowedException;
import org.springfeed.newsfeed.global.error.exception.SelfFollowException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowResponse followUser(Long currentUserId, Long followingId) {

        User currentUser = userRepository.findByIdOrElseThrow(currentUserId);
        User toFollowUser = userRepository.findByIdOrElseThrow(followingId);

        if (currentUserId.equals(followingId)) {
            throw new SelfFollowException();
        }

        if (followRepository.existsByFollowerIdAndFollowingId(currentUserId, followingId)) {
            throw new AlreadyFollowedException();
        }

        Follow follow = new Follow(currentUser, toFollowUser);
        followRepository.save(follow);

        return new FollowResponse(follow);
    }

    // 해당 사용자가 팔로우하는 사용자 목록을 반환한다.
    public FollowingListResponse getFollowingList(Long userId) {
        /*
         * 사용자가 실제로 존재하는지 확인해야 한다.
         * 없는 사용자에 대해 빈 배열을 반환한다면 해당 사용자가 존재하는데 다른 사람을 팔로우 하지 않는 건지
         * 사용자가 없어서 빈 배열을 반환하는 건지 구별할 수 없다.
         */
        userRepository.findByIdOrElseThrow(userId);

        List<Follow> followList = followRepository.findByFollowerIdWithFollowing(userId);

        List<FollowUserResponse> followingUserResponseList = followList.stream()
            .map(Follow::getFollowing) // 팔로우 중인 사용자
            .map(FollowUserResponse::new)
            .toList();

        return new FollowingListResponse(followingUserResponseList);
    }

    // 해당 사용자를 팔로우하는 사용자 목록을 반환한다.
    public FollowerListResponse getFollowerList(Long userId) {
        /*
         * 사용자가 실제로 존재하는지 확인해야 한다.
         * 없는 사용자에 대해 빈 배열을 반환한다면 해당 사용자가 존재하는데
         * 아무도 그 사람을 팔로우하는 않는 건지
         * 사용자가 없어서 빈 배열을 반환하는 건지 구별할 수 없다.
         */
        userRepository.findByIdOrElseThrow(userId);

        List<Follow> followList = followRepository.findByFollowingWithFollower(userId);

        List<FollowUserResponse> followerUserResponseList = followList.stream()
            .map(Follow::getFollower)
            .map(FollowUserResponse::new)
            .toList();

        return new FollowerListResponse(followerUserResponseList);
    }

    @Transactional
    public void unfollowUser(Long currentUserId, Long followingId) {

        Follow follow = followRepository.findByFollowerIdAndFollowingIdOrElseThrow(currentUserId, followingId);

        followRepository.delete(follow);
    }

}
