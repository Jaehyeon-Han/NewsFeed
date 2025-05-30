package org.springfeed.newsfeed.global.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springfeed.newsfeed.global.error.exception.PasswordMismatchException;
import org.springfeed.newsfeed.global.error.exception.UserNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springfeed.newsfeed.constant.UserConstant.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    /*
     * 현재는 AuthService가 PasswordEncoder와 강하게 결합
     * 별도의 인터페이스로 정의해야 유지보수성 향상
     */

    PasswordEncoder passwordEncoder = new PasswordEncoder();

    AuthService authService;



    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder);
    }

    /*
     * 테스트 메소드 이름 규칙
     * Should_ExpectedBehavior_When_StateUnderTest
     * e.g. Should_ThrowException_When_AgeLessThan18
     */

    @Test
    @DisplayName("정상 로그인")
    void should_login_when_userExistsAndPasswordMatches() {
        // given
        User mockUser = new User(
            EMAIL,
            passwordEncoder.encode(PASSWORD),
            NICKNAME,
            INTRODUCTION
        );
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(mockUser));

        // when
        Long login = authService.login(EMAIL, PASSWORD);

        // then
        then(userRepository).should().findByEmail(EMAIL);
        assertThat(login).isEqualTo(1); // User 엔티티의 Id는 설정 불가능
    }

    @Test
    @DisplayName("유저가 없는 경우")
    void should_throwUserNotFoundException_when_userDoesNotExist() {
        // given
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

        // when-then
        assertThrows(UserNotFoundException.class, () -> authService.login(EMAIL, PASSWORD));
        then(userRepository).should().findByEmail(EMAIL);
    }

    @Test
    @DisplayName("비밀번호 불일치")
    void should_throwPasswordMismatchException_when_userExistsAndPasswordDoesNotMatch() {
        // given
        User mockUser = new User(
            EMAIL,
            passwordEncoder.encode(PASSWORD),
            NICKNAME,
            INTRODUCTION
        );
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(mockUser));

        // when-then
        assertThrows(PasswordMismatchException.class, () -> authService.login(EMAIL, NOT_MATCHING_PASSWORD));
        then(userRepository).should().findByEmail(EMAIL);
    }
}