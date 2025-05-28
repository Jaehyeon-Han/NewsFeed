package org.springfeed.newsfeed.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.auth.dto.response.UserResponse;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse login(String email, String password) {

        Optional<User> findUser = userRepository.findByEmail(email);
        if(findUser.isEmpty()) {
            throw new RuntimeException("해당 유저가 없습니다.");
        }
        User user = findUser.get();

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return new UserResponse(user.getId(), user.getNickname());
    }
}
