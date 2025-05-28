package org.springfeed.newsfeed.global.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.global.auth.dto.request.LoginRequest;
import org.springfeed.newsfeed.global.auth.dto.response.UserResponse;
import org.springfeed.newsfeed.global.auth.service.AuthService;
import org.springfeed.newsfeed.global.config.SessionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 구현
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        UserResponse user = authService.login(request.getEmail(), request.getPassword());

        HttpSession session = httpRequest.getSession();
        session.setAttribute(SessionType.USER, user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그아웃 구현
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
