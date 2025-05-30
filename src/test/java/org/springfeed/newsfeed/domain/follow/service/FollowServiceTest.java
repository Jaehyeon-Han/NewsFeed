package org.springfeed.newsfeed.domain.follow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springfeed.newsfeed.domain.entity.Follow;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowerListResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowingListResponse;
import org.springfeed.newsfeed.domain.follow.repository.FollowRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.error.exception.AlreadyFollowedException;
import org.springfeed.newsfeed.global.error.exception.SelfFollowException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    FollowRepository followRepository;
    
    @Mock
    UserRepository userRepository;

    FollowService followService;

    @BeforeEach
    void setUp() {
        followService = new FollowService(followRepository, userRepository);
    }

    private static final long LOGIN_USER_ID = 1L;
    private static final long ANOTHER_USER_ID = 2L;
    private static final long OTHER_USER_ID = 3L;

    @Test
    @DisplayName("정상 팔로우")
    void should_succeed_when_loginUserFollowsAnotherExistingUser() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        User anotherUser = getUserWithId(ANOTHER_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);
        given(userRepository.findByIdOrElseThrow(ANOTHER_USER_ID)).willReturn(anotherUser);
        // 팔로우 하지 않은 상태
        given(followRepository.existsByFollowerIdAndFollowingId(LOGIN_USER_ID, ANOTHER_USER_ID)).willReturn(false);
        Follow follow = new Follow(loginUser, anotherUser);
        long NEW_FOLLOW_ID = 1L;
        ReflectionTestUtils.setField(follow, "id", NEW_FOLLOW_ID);
        given(followRepository.save(any(Follow.class))).willReturn(follow);

        FollowResponse followResponse = followService.followUser(LOGIN_USER_ID, ANOTHER_USER_ID);

        then(followRepository).should().save(any(Follow.class));
        // assertThat(followResponse.getFollowId()).isEqualTo(NEW_FOLLOW_ID);
        /*
         * Follow follow = new Follow(currentUser, toFollowUser);
         * followRepository.save(follow);
         * return new FollowResponse(follow);
         * JPA 없이 id 값을 채울 수 없음
         */
        assertThat(followResponse.getFollowingId()).isEqualTo(ANOTHER_USER_ID);
        assertThat(followResponse.getFollowerId()).isEqualTo(LOGIN_USER_ID);
    }

    @Test
    @DisplayName("자기 자신을 팔로우할 수 없음")
    void should_throwSelfFollowException_when_loginUserFollowThemselves() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);

        assertThatThrownBy(() -> followService.followUser(LOGIN_USER_ID, LOGIN_USER_ID))
            .isInstanceOf(SelfFollowException.class);
    }

    @Test
    @DisplayName("이미 팔로우한 사용자를 다시 팔로우할 수 없음")
    void should_throwAlreadyFollowedException_when_followAlreadyFollowingUser() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        User anotherUser = getUserWithId(ANOTHER_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);
        given(userRepository.findByIdOrElseThrow(ANOTHER_USER_ID)).willReturn(anotherUser);
        // 팔로우한 상태
        given(followRepository.existsByFollowerIdAndFollowingId(LOGIN_USER_ID, ANOTHER_USER_ID)).willReturn(true);

        assertThatThrownBy(() -> followService.followUser(LOGIN_USER_ID, ANOTHER_USER_ID))
            .isInstanceOf(AlreadyFollowedException.class);
    }

    @Test
    @DisplayName("팔로우하는 사용자가 없다면 빈 리스트 빈환")
    void should_returnEmptyList_when_loginUserFollowingNoOne() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);
        given(followRepository.findByFollowerId(LOGIN_USER_ID)).willReturn(List.of());

        FollowingListResponse followings = followService.getFollowings(LOGIN_USER_ID);

        assertThat(followings.getTotalFollowingCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("팔로우하는 사용자가 있다면 팔로우 중인 사용자 리스트 빈환")
    void should_returnFollowingList_when_loginUserFollowingOthers() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);
        User anotherUser = getUserWithId(ANOTHER_USER_ID);
        User otherUser = getUserWithId(OTHER_USER_ID);

        Follow follow1 = new Follow(loginUser, anotherUser);
        Follow follow2 = new Follow(loginUser, otherUser);
        given(followRepository.findByFollowerId(LOGIN_USER_ID)).willReturn(List.of(follow1, follow2));

        FollowingListResponse followings = followService.getFollowings(LOGIN_USER_ID);

        assertThat(followings.getFollowings()).anySatisfy(followUserResponse -> {
            assertThat(followUserResponse.getId()).isEqualTo(ANOTHER_USER_ID);
        });
        assertThat(followings.getTotalFollowingCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("자신의 팔로워가 없다면 빈 리스트 빈환")
    void should_returnEmptyList_when_loginUserFollowedByNoOne() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);
        given(followRepository.findByFollowingId(LOGIN_USER_ID)).willReturn(List.of());

        FollowerListResponse followers = followService.getFollowers(LOGIN_USER_ID);

        assertThat(followers.getTotalFollowerCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("자신을 팔로우하는 사용자가 있다면 팔로워 리스트 빈환")
    void should_returnFollowerList_when_loginUserFollowedByOthers() {
        User loginUser = getUserWithId(LOGIN_USER_ID);
        given(userRepository.findByIdOrElseThrow(LOGIN_USER_ID)).willReturn(loginUser);
        User anotherUser = getUserWithId(ANOTHER_USER_ID);
        User otherUser = getUserWithId(OTHER_USER_ID);

        Follow follow1 = new Follow(anotherUser, loginUser);
        Follow follow2 = new Follow(otherUser, loginUser);
        given(followRepository.findByFollowingId(LOGIN_USER_ID)).willReturn(List.of(follow1, follow2));

        FollowerListResponse followers = followService.getFollowers(LOGIN_USER_ID);

        assertThat(followers.getFollowers()).anySatisfy(followUserResponse -> {
            assertThat(followUserResponse.getId()).isEqualTo(ANOTHER_USER_ID);
        });
        assertThat(followers.getTotalFollowerCount()).isEqualTo(2);
    }

    private User getUserWithId(long userId) {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }
}