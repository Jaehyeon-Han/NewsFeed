package org.springfeed.newsfeed.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.post.dto.request.CreatePostRequest;
import org.springfeed.newsfeed.domain.post.dto.request.UpdatePostRequest;
import org.springfeed.newsfeed.domain.post.dto.response.PostResponse;
import org.springfeed.newsfeed.domain.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/new")
    public ResponseEntity<PostResponse> create(
            @RequestBody CreatePostRequest createPostRequest,
            @SessionAttribute(name = "user") Object userData
    ) {
        PostResponse postResponse = postService.save(
                createPostRequest.getTitle(),
                createPostRequest.getContent(),
                1L  // Fixme
        );

        return ResponseEntity.ok(postResponse);
    }

    // 전체 게시글 페이지 조회
    @GetMapping // Fixme: 페이지를 구현하려면 ? 대신 들어갈 자료구조는?
    public ResponseEntity<Page<PostResponse>> getPostResponsePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy
            // Todo: 페이징 객체 내부에서 정렬기준을 받으려면?
    ) {
        // Todo: 서비스 객체는 어떤 타입을 반환해야 할까?
        int size = 10;

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.Direction.DESC,
                sortBy
        );

        Page<PostResponse> postPage = postService.getPostPage(pageable);

        return ResponseEntity.ok(postPage);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(
            @PathVariable long id) {
        PostResponse postResponse = postService.findById(id);

        return ResponseEntity.ok(postResponse);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updateById(
            @PathVariable(name = "id") long postId,
            @SessionAttribute(name = "user") Object userData, // Fixme
            @RequestBody UpdatePostRequest request
    ) {
        PostResponse postResponse = postService.updateById(
                postId,
                1L, // Fixme
                request.getTitle(),
                request.getContent());

        return ResponseEntity.ok(postResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @SessionAttribute(name = "user") Object userData, // Fixme
            @PathVariable(name = "id") long postId) {

        postService.deleteById(postId, 1L);
        return ResponseEntity.ok().build();
    }
}
