package org.springfeed.newsfeed.domain.follow.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.follow.dto.request.FollowRequest;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowerListResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowingListResponse;
import org.springfeed.newsfeed.domain.follow.service.FollowService;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<FollowResponse> followUser(HttpServletRequest httpRequest,
        @RequestBody @Valid FollowRequest followRequest
    ) {

        Long currentUserId = jwtUtil.getUserId(httpRequest);

        FollowResponse followResponse = followService.followUser(currentUserId, followRequest.getFollowingId());

        return ResponseEntity.status(HttpStatus.OK).body(followResponse);
    }

    // 해당 사용자가 팔로우하는 사용자 목록을 반환한다.
    @GetMapping("/followings/users/{userId}")
    public ResponseEntity<FollowingListResponse> getFollowingList(@PathVariable Long userId) {

        FollowingListResponse followingList = followService.getFollowingList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(followingList);
    }

    // 해당 사용자를 팔로우하는 사용자 목록을 반환한다.
    @GetMapping("/followers/users/{userId}")
    public ResponseEntity<FollowerListResponse> getFollowerList(@PathVariable Long userId) {

        FollowerListResponse followerList = followService.getFollowerList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(followerList);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollowUser(HttpServletRequest httpRequest,
        @PathVariable Long followingId
    ) {

        Long currentUserId = jwtUtil.getUserId(httpRequest);

        followService.unfollowUser(currentUserId, followingId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
