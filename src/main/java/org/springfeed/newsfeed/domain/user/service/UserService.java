package org.springfeed.newsfeed.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.dto.request.ChangePasswordRequest;
import org.springfeed.newsfeed.domain.user.dto.request.UpdateUserInfoRequest;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springfeed.newsfeed.global.error.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 가입
    @Transactional
    public void signUp(String email, String password, String passwordCheck, String nickname, String introduction) {

        if (!password.equals(passwordCheck)) {
            throw new PasswordCheckFailException();
        }
        String encodePassword = passwordEncoder.encode(password);

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateUserException();
        }

        User user = new User(email, encodePassword, nickname, introduction);
        userRepository.save(user);
    }

    //회원 탈퇴
    @Transactional
    public void delete(Long userId, String password, Long currentUser) {

        verifyUserIdentityOrThrow(userId, currentUser);

        User foundUser = userRepository.findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(password, foundUser.getPasswordHash())) {
            throw new PasswordMismatchException();
        }

        userRepository.delete(foundUser);
    }

    // 유저 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return new UserResponse(user);
    }

    // 유저 정보 수정
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserInfoRequest request, Long currentUser) {

        verifyUserIdentityOrThrow(userId, currentUser);

        User user = userRepository.findByIdOrElseThrow(userId);
        user.setNickname(request.getNickname());
        user.setIntroduction(request.getIntroduction());

        return new UserResponse(user);
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(Long userId, ChangePasswordRequest request, Long currentUser) {

        verifyUserIdentityOrThrow(userId, currentUser);

        User user = userRepository.findByIdOrElseThrow(userId);

        // 기존 비밀번호 불일치
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new PasswordMismatchException();
        }

        // 변경하려는 비밀번호가 기존과 동일
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new PasswordUnchangedException();
        }

        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        user.setPasswordHash(newPasswordHash);
    }

    // 현재 세션의 id와 요청의 id가 동일한지 확인
    private void verifyUserIdentityOrThrow(Long userId, Long currentUser) {
        if (!currentUser.equals(userId)) {
            throw new AccessDeniedException("본인이 아닙니다.");
        }
    }

}
