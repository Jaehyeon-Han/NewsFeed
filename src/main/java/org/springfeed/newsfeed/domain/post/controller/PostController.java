package org.springfeed.newsfeed.domain.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.post.dto.request.CreatePostRequest;
import org.springfeed.newsfeed.domain.post.dto.request.UpdatePostRequest;
import org.springfeed.newsfeed.domain.post.dto.response.PostResponse;
import org.springfeed.newsfeed.domain.post.service.PostService;

import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    // 게시글 작성
    @PostMapping("/new")
    public ResponseEntity<PostResponse> create(
        @Valid @RequestBody CreatePostRequest createPostRequest,
        HttpServletRequest httpRequest) {

        Long currentId = jwtUtil.getUserId(httpRequest);
        PostResponse postResponse = postService.save(
            createPostRequest.getTitle(),
            createPostRequest.getContent(),
            currentId
        );

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    // 전체 게시글 페이지 조회
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPostResponsePage(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        int size = 10;

        Pageable pageable = PageRequest.of(
            page - 1,
            size,
            Sort.by(Sort.Direction.fromString(sortDir),
                sortBy)
        );

        Page<PostResponse> postPage = postService.getPostPage(pageable, startDate, endDate);

        // Request에서 받은 페이지값을 그대로 반환해주기 위해 커스텀 페이지 생성.
        Pageable customPageable = PageRequest.of(page, size, pageable.getSort());

        Page<PostResponse> customPage = new PageImpl<>(
                postPage.getContent(),
                customPageable,
                postPage.getTotalElements()
        );

        return ResponseEntity.status(HttpStatus.OK).body(customPage);
    }

    // 팔로우 한 사람의 게시글 조회
    @GetMapping("/following")
    public ResponseEntity<Page<PostResponse>> getPostFollowingPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @SessionAttribute(name = SessionType.USER) Long currentId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.fromString(sortDir),
                        sortBy)
        );

        Page<PostResponse> postPage = postService.getPostFollowingPage(pageable, currentId);

        // Request에서 받은 페이지값을 그대로 반환해주기 위해 커스텀 페이지 생성.
        Pageable customPageable = PageRequest.of(page, size, pageable.getSort());

        Page<PostResponse> customPage = new PageImpl<>(
                postPage.getContent(),
                customPageable,
                postPage.getTotalElements()
        );

        return ResponseEntity.status(HttpStatus.OK).body(customPage);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(
        @PathVariable long id) {
        PostResponse postResponse = postService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updateById(
        @PathVariable(name = "id") long postId,
        HttpServletRequest httpRequest,
        @RequestBody @Valid UpdatePostRequest request) {

        Long currentId = jwtUtil.getUserId(httpRequest);
        PostResponse postResponse = postService.updateById(
            postId,
            currentId,
            request.getTitle(),
            request.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            HttpServletRequest httpRequest,
            @PathVariable(name = "id") long postId) {

        Long currentId = jwtUtil.getUserId(httpRequest);
        postService.deleteById(postId, currentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
