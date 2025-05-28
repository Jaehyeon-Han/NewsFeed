package org.springfeed.newsfeed.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.config.PasswordEncoder;
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

    // Todo
}
