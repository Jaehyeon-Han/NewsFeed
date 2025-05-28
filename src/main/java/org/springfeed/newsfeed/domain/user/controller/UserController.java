package org.springfeed.newsfeed.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.user.dto.request.ChangePasswordRequest;
import org.springfeed.newsfeed.domain.user.dto.request.DeleteAccountRequest;
import org.springfeed.newsfeed.domain.user.dto.request.SignUpRequest;
import org.springfeed.newsfeed.domain.user.dto.request.UpdateUserInfoRequest;
import org.springfeed.newsfeed.domain.user.dto.response.UserResponse;
import org.springfeed.newsfeed.domain.user.service.UserService;
import org.springfeed.newsfeed.global.auth.dto.response.SessionUserDataResponse;
import org.springfeed.newsfeed.global.config.SessionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 유저 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUser(userId);

        return ResponseEntity.ok(userResponse);
    }

    // 유저 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserInfoRequest request,
            @SessionAttribute(name = SessionType.USER) SessionUserDataResponse currentUser) {

        try {
            UserResponse response = userService.updateUser(userId, request, currentUser);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //비밀번호 수정
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @Valid @RequestBody ChangePasswordRequest request,
            @SessionAttribute(name = SessionType.USER) SessionUserDataResponse currentUser) {

        userService.updatePassword(userId, request, currentUser);
        return ResponseEntity.ok().build();
    }


    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request.getEmail(), request.getPassword(), request.getPasswordCheck(), request.getNickname(), request.getIntroduction());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 회원 탈퇴
    @PostMapping("/users/{userId}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @Valid @RequestBody DeleteAccountRequest request, HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        SessionUserDataResponse userData = (SessionUserDataResponse) session.getAttribute(SessionType.USER);

        userService.delete(userId, request.getPassword(), userData);
        session.invalidate();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
