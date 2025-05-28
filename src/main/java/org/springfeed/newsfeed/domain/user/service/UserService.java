package org.springfeed.newsfeed.domain.user.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.dto.request.ChangePasswordRequest;
import org.springfeed.newsfeed.domain.user.dto.request.UpdateUserInfoRequest;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.auth.dto.response.SessionResponse;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springfeed.newsfeed.global.config.SessionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(String email, String password, String passwordCheck, String nickname, String introduction) {
        if(!password.equals(passwordCheck)) {
            throw new RuntimeException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        String encodePassword = passwordEncoder.encode(password);
        Optional<User> exist = userRepository.findByEmail(email);
        if(exist.isPresent()) {
            throw new RuntimeException("존재하는 이메일입니다.");
        }
        User user = new User(email, encodePassword, nickname, introduction);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId, String password) {
        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isEmpty()) {
            throw new RuntimeException("아이디를 찾을 수 없습니다.");
        }
        User user = findUser.get();
        if(!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }


    // 유저 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserResponse(user);
    }


    // 유저 정보 수정
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserInfoRequest request, SessionResponse currentUser) {

        // 1. 본인 확인
        if (!currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException("본인이 아닙니다.");
        }

        // 2. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 수정
        user.setNickname(request.getNickname());
        user.setIntroduction(request.getIntroduction());

        return new UserResponse(user);

    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(Long userId, ChangePasswordRequest request, SessionResponse currentUser) {

        // 1. 본인 확인
        if (!currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException("본인만 비밀번호를 수정할 수 있습니다.");
        }

        // 3. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));


        // 4. 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 5. 동일한 비밀번호인지 확인
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 같을 수 없습니다.");
        }

        // 6. 새 비밀번호 암호화 후 저장
        String encoded = passwordEncoder.encode(request.getNewPassword());
        user.setPasswordHash(encoded);
    }



}
