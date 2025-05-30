package org.springfeed.newsfeed.domain.follow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.follow.dto.request.FollowRequest;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowerListResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowingListResponse;
import org.springfeed.newsfeed.domain.follow.service.FollowService;
import org.springfeed.newsfeed.global.config.SessionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<FollowResponse> followUser(
            @SessionAttribute (name = SessionType.USER) Long currentUserId,
            @RequestBody @Valid FollowRequest followRequest){

        FollowResponse followResponse = followService.followUser(currentUserId, followRequest.getFollowingId());

        return ResponseEntity.status(HttpStatus.OK).body(followResponse);
    }

    // 해당 사용자가 팔로우하는 사용자 목록을 반환한다.
    @GetMapping("/followings/{userId}")
    public ResponseEntity<FollowingListResponse> getFollowings(@PathVariable Long userId){
        FollowingListResponse followings = followService.getFollowings(userId);

        return ResponseEntity.status(HttpStatus.OK).body(followings);
    }

    // 해당 사용자를 팔로우하는 사용자 목록을 반환한다.
    @GetMapping("/followers/{userId}")
    public ResponseEntity<FollowerListResponse> getFollowers(@PathVariable Long userId){
        FollowerListResponse Followers = followService.getFollowers(userId);

        return ResponseEntity.status(HttpStatus.OK).body(Followers);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @SessionAttribute (name = SessionType.USER) Long currentUserId,
            @PathVariable Long followingId){
        followService.unfollowUser(currentUserId, followingId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
