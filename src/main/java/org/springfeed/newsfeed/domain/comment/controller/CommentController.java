package org.springfeed.newsfeed.domain.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.comment.dto.request.CommentRequest;
import org.springfeed.newsfeed.domain.comment.dto.response.CommentResponse;
import org.springfeed.newsfeed.domain.comment.service.CommentService;
import org.springfeed.newsfeed.global.config.SessionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> create(@PathVariable Long postId,
                                                  @RequestBody CommentRequest request,
                                                  @SessionAttribute(name = SessionType.USER) Long userId) {
        CommentResponse comment = commentService.create(userId, postId, request.getComment());
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    //게시글에 있는 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> findById(@PathVariable Long postId) {
        List<CommentResponse> comment = commentService.findById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    //댓글 수정
    @PatchMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> updateById(@PathVariable Long id,
                                                      @RequestBody CommentRequest request,
                                                      @SessionAttribute(name = SessionType.USER) Long userId) {
        CommentResponse comment = commentService.updateById(id, userId, request.getComment());
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @SessionAttribute(name = SessionType.USER) Long userId) {
        commentService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
