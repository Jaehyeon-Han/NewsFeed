package org.springfeed.newsfeed.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.dto.request.UpdateUserInfoRequest;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);
//                .orElseThrow(() -> new NotFoundException("USER 정보가 존재하지 않습니다."));  // 유저 찾을 수 없는 경우

        return new UserResponse(user);
    }

    // 유저 정보 수정
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserInfoRequest request) {

        User user = userRepository.findById(userId).orElse(null);

        user.setNickname(request.getNickname());
        user.setIntroduction(request.getIntroduction());

        return new UserResponse(user);

    }


}
