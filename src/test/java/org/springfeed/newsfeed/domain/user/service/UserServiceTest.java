package org.springfeed.newsfeed.domain.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springfeed.newsfeed.global.error.exception.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springfeed.newsfeed.constant.UserConstant.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    UserService userService;
    PasswordEncoder passwordEncoder = new PasswordEncoder();

    private static final long LOGIN_ID = 1L;
    private static final long OTHER_USER_ID = 2L;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    /*
     * 테스트 메소드 이름 규칙
     * Should_ExpectedBehavior_When_StateUnderTest
     * e.g. Should_ThrowException_When_AgeLessThan18
     */

    // 회원가입

    @Test
    @DisplayName("정상 가입")
    void should_signUp_with_uniqueEmailAndMatchingPasswordCheck() {
        // given
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

        // when
        userService.signUp(EMAIL, PASSWORD, PASSWORD, NICKNAME, INTRODUCTION);

        // then
        then(userRepository).should().findByEmail(EMAIL);
    }

    @Test
    @DisplayName("가입 시 비밀번호 확인 불일치")
    void should_throwPasswordCheckFailException_when_signUpWithNotMatchingPasswordCheck() {
        // given
        String notMatchingPasswordCheck = "notMatchingPassword2@";

        // when-then
        assertThatThrownBy(() -> userService.signUp(EMAIL, PASSWORD, notMatchingPasswordCheck, NICKNAME, INTRODUCTION))
            .isInstanceOf(PasswordCheckFailException.class);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 가입")
    void should_throwDuplicateUserException_when_signUpWithExistingEmail() {
        // given
        User mockUser = new User(
            EMAIL,
            passwordEncoder.encode(PASSWORD),
            NICKNAME,
            INTRODUCTION
        );
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(mockUser));

        // when-then
        assertThatThrownBy(() -> userService.signUp(EMAIL, PASSWORD, PASSWORD, NICKNAME, INTRODUCTION))
            .isInstanceOf(DuplicateUserException.class);
        then(userRepository).should().findByEmail(EMAIL);
    }

    // 회원탈퇴

    @Test
    @DisplayName("정상 탈퇴")
    void should_deleteAccount_when_deleteLoggedInAccountWithValidPassword() {
        // given
        User mockUser = getMockUserWithLoginId();
        given(userRepository.findByIdOrElseThrow(LOGIN_ID)).willReturn(mockUser);

        // when
        userService.delete(LOGIN_ID, PASSWORD, LOGIN_ID);

        // then
        then(userRepository).should().findByIdOrElseThrow(LOGIN_ID);
        then(userRepository).should().delete(mockUser);
    }

    @Test
    @DisplayName("다른 사용자에 대한 요청")
    // private 메소드인 verifyUserIdentityOrThrow() 에 대한 검증
    void should_throwAccessDeniedException_when_accessingAccountOfOthers() {
        // when-then
        assertThatThrownBy(() -> userService.delete(OTHER_USER_ID, PASSWORD, LOGIN_ID))
            .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("기존 비밀번호와 다른 비밀번호로 요청")
    void test() {
        User mockUser = getMockUserWithLoginId();
        given(userRepository.findByIdOrElseThrow(LOGIN_ID)).willReturn(mockUser);

        assertThatThrownBy(() -> userService.delete(LOGIN_ID, NOT_MATCHING_PASSWORD, LOGIN_ID))
            .isInstanceOf(PasswordMismatchException.class);
        then(userRepository).should().findByIdOrElseThrow(LOGIN_ID);
    }

    // 비밀번호 수정

    @Test
    @DisplayName("비밀번호 정상 수정")
    void should_changePassword_when_loginUserChangesPasswordToAnotherPassword() {
        // given
        User mockUser = getMockUserWithLoginId();
        given(userRepository.findByIdOrElseThrow(LOGIN_ID)).willReturn(mockUser);

        // when
        userService.updatePassword(LOGIN_ID, PASSWORD, NOT_MATCHING_PASSWORD, LOGIN_ID);

        // then
        assertThat(mockUser.getPasswordHash()).isNotEqualTo(passwordEncoder.encode(PASSWORD));
        then(userRepository).should().findByIdOrElseThrow(LOGIN_ID);
    }

    @Test
    @DisplayName("동일 비밀번호로 수정")
    void should_throwPasswordUnchangedException_when_loginUserChangesPasswordToSamePassword() {
        // given
        User mockUser = getMockUserWithLoginId();
        given(userRepository.findByIdOrElseThrow(LOGIN_ID)).willReturn(mockUser);

        // when
        assertThatThrownBy(() -> userService.updatePassword(LOGIN_ID, PASSWORD, PASSWORD, LOGIN_ID))
            .isInstanceOf(PasswordUnchangedException.class);
        then(userRepository).should().findByIdOrElseThrow(LOGIN_ID);
    }

    private User getMockUserWithLoginId() {
        User mockUser = new User(EMAIL, passwordEncoder.encode(PASSWORD), NICKNAME, INTRODUCTION);
        ReflectionTestUtils.setField(mockUser, "id", LOGIN_ID);
        return mockUser;
    }
}