package org.springfeed.newsfeed.domain.user.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.dto.request.ChangePasswordRequest;
import org.springfeed.newsfeed.domain.user.dto.request.UpdateUserInfoRequest;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springfeed.newsfeed.global.config.PasswordEncoder;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);
//                .orElseThrow(() -> new NotFoundException("USER 정보가 존재하지 않습니다."));  // 유저 찾을 수 없는 경우

        return new UserResponse(user);
    }

    // 유저 정보 수정
    @Transactional
    public UserResponse updateUser(HttpSession session, Long userId, UpdateUserInfoRequest request) {

        // 1. 세션에서 로그인된 사용자 ID 확인
        Long currentUserId = (Long) session.getAttribute("userId");

        if (currentUserId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 2. 본인 확인
        if (!currentUserId.equals(userId)) {
            throw new IllegalArgumentException("본인만 수정할 수 있습니다.");
        }

        // 3. 유저 조회
        User user = userRepository.findById(userId).orElse(null);

//        if (user == null || user.isDeleted()) {
//            return null; // 예외처리는 나중에
//        }

        // 4. 수정
        user.setNickname(request.getNickname());
        user.setIntroduction(request.getIntroduction());

        return new UserResponse(user);

    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(HttpSession session, Long userId, ChangePasswordRequest request) {
        // 1. 로그인 확인
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 2. 본인 확인
        if (!currentUserId.equals(userId)) {
            throw new IllegalArgumentException("본인만 비밀번호를 수정할 수 있습니다.");
        }

        // 3. 사용자 조회
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

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
