package org.springfeed.newsfeed.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    // User  조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUser(userId);

        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(userResponse);
    }
    
    // 회원가입과 탈퇴 여기에 구현
    // Todo
}
