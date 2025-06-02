package org.springfeed.newsfeed.global.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.global.auth.dto.request.LoginRequest;
import org.springfeed.newsfeed.global.auth.service.AuthService;
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
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {

        String jwt = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", jwt).body(jwt);
    }

}
