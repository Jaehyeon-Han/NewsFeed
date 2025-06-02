package org.springfeed.newsfeed.domain.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.comment.dto.request.CommentRequest;
import org.springfeed.newsfeed.domain.comment.dto.response.CommentResponse;
import org.springfeed.newsfeed.domain.comment.service.CommentService;
import org.springfeed.newsfeed.global.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    //댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> create(
        @PathVariable Long postId,
        @RequestBody CommentRequest request,
        HttpServletRequest httpRequest
    ) {

        Long currentId = jwtUtil.getUserId(httpRequest);

        CommentResponse comment = commentService.create(currentId, postId, request.getComment());

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    //게시글에 있는 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllInPost(@PathVariable Long postId) {

        List<CommentResponse> commentList = commentService.getAllInPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    //댓글 수정
    @PatchMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> updateById(
        @PathVariable Long id,
        @RequestBody CommentRequest request,
        HttpServletRequest httpRequest
    ) {

        Long currentId = jwtUtil.getUserId(httpRequest);

        CommentResponse comment = commentService.updateById(id, currentId, request.getComment());

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, HttpServletRequest httpRequest) {

        Long currentId = jwtUtil.getUserId(httpRequest);

        commentService.delete(id, currentId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
