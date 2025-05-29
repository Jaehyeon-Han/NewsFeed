package org.springfeed.newsfeed.domain.follow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.follow.dto.request.FollowRequest;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowerListResponse;
import org.springfeed.newsfeed.domain.follow.dto.response.FollowingListResponse;
import org.springfeed.newsfeed.domain.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<FollowResponse> followUser(@RequestBody @Valid FollowRequest followRequest){
        FollowResponse followResponse = followService.followUser(followRequest.getFollowingId());

        return ResponseEntity.status(HttpStatus.CREATED).body(followResponse);
    }

    @GetMapping("/followings/{userId}")
    public ResponseEntity<FollowingListResponse> getFollowings(@PathVariable Long userId){
        FollowingListResponse followings = followService.getFollowings(userId);

        return ResponseEntity.status(HttpStatus.OK).body(followings);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<FollowerListResponse> getFollowers(@PathVariable Long userId){
        FollowerListResponse Followers = followService.getFollowers(userId);

        return ResponseEntity.status(HttpStatus.OK).body(Followers);
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followingId){
        followService.unfollowUser(followingId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
