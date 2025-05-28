package org.springfeed.newsfeed.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.user.dto.request.DeleteAccountRequest;
import org.springfeed.newsfeed.domain.user.dto.request.SignUpRequest;
import org.springfeed.newsfeed.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    // Todo

    // 회원가입과 탈퇴 여기에 구현
    // Todo
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request.getEmail(), request.getPassword(), request.getPasswordCheck(), request.getNickname(), request.getIntroduction());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/{userId}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestBody DeleteAccountRequest request) {
        userService.delete(userId, request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
