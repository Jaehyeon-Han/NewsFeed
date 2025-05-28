package org.springfeed.newsfeed.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);
//                .orElseThrow(() -> new NotFoundException("USER 정보가 존재하지 않습니다."));  // 유저 찾을 수 없는 경우

        return new UserResponse(user);
    }

}
