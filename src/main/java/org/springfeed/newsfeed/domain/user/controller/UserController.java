package org.springfeed.newsfeed.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.user.dto.request.ChangePasswordRequest;
import org.springfeed.newsfeed.domain.user.dto.request.DeleteAccountRequest;
import org.springfeed.newsfeed.domain.user.dto.request.SignUpRequest;
import org.springfeed.newsfeed.domain.user.dto.request.UpdateUserInfoRequest;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.service.UserService;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 유저 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {

        UserResponse userResponse = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    // 유저 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable Long userId,
        @Valid @RequestBody UpdateUserInfoRequest request,
        HttpServletRequest httpRequest
    ) {

        Long currentId = jwtUtil.getUserId(httpRequest);

        UserResponse response = userService.updateUser(userId,
            request.getNickname(),
            request.getIntroduction(),
            currentId
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //비밀번호 수정
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<Void> updatePassword(
        @PathVariable Long userId,
        @Valid @RequestBody ChangePasswordRequest request,
        HttpServletRequest httpRequest
    ) {

        Long currentId = jwtUtil.getUserId(httpRequest);

        userService.updatePassword(userId, request.getCurrentPassword(), request.getNewPassword(), currentId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {

        userService.signUp(
            request.getEmail(),
            request.getPassword(),
            request.getPasswordCheck(),
            request.getNickname(),
            request.getIntroduction()
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원 탈퇴
    @PostMapping("/users/{userId}/delete")
    public ResponseEntity<Void> delete(
        @PathVariable Long userId,
        @Valid @RequestBody DeleteAccountRequest request,
        HttpServletRequest httpRequest
    ) {

        Long currentId = jwtUtil.getUserId(httpRequest);

        userService.delete(userId, request.getPassword(), currentId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
