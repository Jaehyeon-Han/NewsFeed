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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        @RequestParam(defaultValue = "desc") String sortDir
    ) {
        int size = 10;

        Pageable pageable = PageRequest.of(
            page - 1,
            size,
            Sort.by(Sort.Direction.fromString(sortDir),
                sortBy)
        );

        // Fixme: 현재 응답 페이지 0부터 시작함
        Page<PostResponse> postPage = postService.getPostPage(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(postPage);
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
