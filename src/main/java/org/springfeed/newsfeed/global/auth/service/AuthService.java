package org.springfeed.newsfeed.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
import org.springfeed.newsfeed.global.error.exception.PasswordMismatchException;
import org.springfeed.newsfeed.global.error.exception.UserNotFoundException;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(String email, String password) {

        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = findUser.get();

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new PasswordMismatchException();
        }

        return jwtUtil.createToken(user.getId());
    }
}
