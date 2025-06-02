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

        Comment newComment = new Comment(post, user, comment);
        commentRepository.save(newComment);

        return new CommentResponse(newComment);
    }

    // 댓글 조회
    public List<CommentResponse> getAllInPost(Long postId) {

        postRepository.findPostByIdOrElseThrow(postId);

        List<Comment> comments = commentRepository.findAllByPostId(postId).orElse(List.of());

        return comments.stream()
            .map(CommentResponse::new)
            .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateById(Long id, Long userId, String comment) {

        Comment foundComment = findAndCheckAuthorization(id, userId);

        foundComment.setComment(comment);

        return new CommentResponse(foundComment);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long id, Long userId) {

        Comment Comment = findAndCheckAuthorization(id, userId);

        commentRepository.delete(Comment);
    }

    // 댓글, 권한 확인
    private Comment findAndCheckAuthorization(Long id, Long userId) {

        Comment foundComment = commentRepository.findByIdOrElseThrow(id);

        boolean isCommentAuthor = foundComment.getAuthor().getId().equals(userId);
        boolean isPostAuthor = foundComment.getPost().getAuthor().getId().equals(userId);

        if (!(isCommentAuthor || isPostAuthor)) {
            throw new AccessDeniedException();
        }

        return foundComment;
    }

}
