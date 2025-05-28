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
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUser(userId);

        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(userResponse);
    }

    // 유저 수정
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserInfoRequest request, HttpSession session) {

        try {
            UserResponse response = userService.updateUser(session, userId, request);

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            // 로그인 안된 상태
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            // 본인 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        }
    }

    //비밀번호 수정
    @PatchMapping("/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest request,
            HttpSession session
    ) {
        try {
            userService.updatePassword(session, userId, request);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");

        } catch (IllegalStateException e) {
            // 로그인 안된 상태
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            // 비밀번호 틀림, 본인 아님, 동일한 비밀번호 등
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }


    // 회원가입과 탈퇴 여기에 구현
    // Todo
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        userService.signUp(request.getEmail(), request.getPassword(), request.getPasswordCheck(), request.getNickname(), request.getIntroduction());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/{userId}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @Valid @RequestBody DeleteAccountRequest request, HttpServletRequest httpRequest) {
        userService.delete(userId, request.getPassword());
        HttpSession session = httpRequest.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
