package org.springfeed.newsfeed.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.comment.dto.response.CommentResponse;
import org.springfeed.newsfeed.domain.comment.repository.CommentRepository;
import org.springfeed.newsfeed.domain.entity.Comment;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.post.repository.PostRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.error.exception.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse create(Long userId, Long postId, String comment) {

        Post post = postRepository.findPostByIdOrElseThrow(postId);
        User user = userRepository.findByIdOrElseThrow(userId);

        Comment createComment = new Comment(post, user, comment);
        commentRepository.save(createComment);

        return new CommentResponse(createComment.getId(),
            createComment.getPost().getId(),
            createComment.getAuthor().getId(),
            createComment.getAuthor().getNickname(),
            createComment.getComment(),
            createComment.getCreatedAt(),
            createComment.getLastModifiedAt());
    }

    // 댓글 조회
    public List<CommentResponse> findById(Long postId) {

        postRepository.findPostByIdOrElseThrow(postId);

        List<Comment> comments = commentRepository.findAllByPost_Id(postId).orElse(List.of());

        return comments.stream().map(comment -> new CommentResponse(comment.getId(),
            comment.getPost().getId(),
            comment.getAuthor().getId(),
            comment.getAuthor().getNickname(),
            comment.getComment(),
            comment.getCreatedAt(),
            comment.getLastModifiedAt())).toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateById(Long id, Long userId, String comment) {

        Comment findComment = findAndCheckComment(id, userId);

        findComment.setComment(comment);

        return new CommentResponse(findComment.getId(),
            findComment.getPost().getId(),
            findComment.getAuthor().getId(),
            findComment.getAuthor().getNickname(),
            findComment.getComment(),
            findComment.getCreatedAt(),
            findComment.getLastModifiedAt());
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long id, Long userId) {

        Comment Comment = findAndCheckComment(id, userId);

        commentRepository.delete(Comment);
    }

    // 댓글, 권한 확인
    private Comment findAndCheckComment(Long id, Long userId) {

        Comment findComment = commentRepository.findByIdOrElseThrow(id);

        if (!(findComment.getAuthor().getId().equals(userId) || findComment.getPost().getAuthor().getId().equals(userId))) {
            throw new AccessDeniedException();
        }
        return findComment;
    }

}
