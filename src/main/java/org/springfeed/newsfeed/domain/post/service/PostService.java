package org.springfeed.newsfeed.domain.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.post.dto.response.PostResponse;
import org.springfeed.newsfeed.domain.post.repository.PostRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springfeed.newsfeed.global.error.exception.AccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse save(String title, String content, long userId) {

        User foundUser = userRepository.findByIdOrElseThrow(userId);

        Post post = new Post(title, content);
        post.setUser(foundUser);

        Post createdPost = postRepository.save(post);

        return new PostResponse(
            createdPost.getId(),
            createdPost.getTitle(),
            createdPost.getContents(),
            foundUser.getId(),
            foundUser.getNickname(),
            createdPost.getCreatedAt(),
            createdPost.getLastModifiedAt()
        );
    }

    public PostResponse findById(long id) {
        Post foundPost = postRepository.findPostByIdOrElseThrow(id);
        User author = foundPost.getAuthor();

        return new PostResponse(
            foundPost.getId(),
            foundPost.getTitle(),
            foundPost.getContents(),
            author.getId(),
            author.getNickname(),
            foundPost.getCreatedAt(),
            foundPost.getLastModifiedAt()
        );
    }

    @Transactional
    public PostResponse updateById(long postId, long userId, String title, String content) {
        Post foundPost = postRepository.findPostByIdOrElseThrow(postId);
        User author = foundPost.getAuthor();

        verifyAuthorOrThrow(userId, author);

        foundPost.setTitle(title);
        foundPost.setContents(content);

        /*
            1. UPDATE 쿼리가 DB에 실행됨
            2. Hibernate가 DB에서 자동 생성된 값들을 다시 가져와서
            3. 1차 캐시의 엔티티 객체도 함께 업데이트함
            4. 업데이트된 getLastModifiedAt를 PostResponse에 넘겨줌
         */
        postRepository.flush();

        return new PostResponse(
            foundPost.getId(),
            foundPost.getTitle(),
            foundPost.getContents(),
            author.getId(),
            author.getNickname(),
            foundPost.getCreatedAt(),
            foundPost.getLastModifiedAt()
        );
    }

    public void deleteById(long postId, long userId) {
        Post foundPost = postRepository.findPostByIdOrElseThrow(postId);

        verifyAuthorOrThrow(userId, foundPost.getAuthor());

        postRepository.delete(foundPost);
    }

    public Page<PostResponse> getPostPage(Pageable pageable, LocalDate startDate, LocalDate endDate) {

        if(startDate != null && endDate != null)
        {
            // atStartOfDay(), atTime() 메소드는 LocalDate -> LocalDateTime로 변환가능한 메소드.
            // 오늘이 2025-05-30이라고 했을때 startDateTime은 2025-05-30T00:00:00
            LocalDateTime startDateTime = startDate.atStartOfDay();

            // 오늘이 2025-05-30이라고 했을때 startDateTime은 2025-05-30T23:59:59
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);

            Page<Post> postPage = postRepository.findAllByCreatedAtBetween(startDateTime, endDateTime, pageable);
            return postPage.map(this::convertToResponse);
        }
        else {
            Page<Post> postPage = postRepository.findAll(pageable);
            return postPage.map(this::convertToResponse);
        }
    }

    // Post를 PostResponse로 변환
    private PostResponse convertToResponse(Post post) {
        return PostResponse.builder()
            .postId(post.getId())
            .title(post.getTitle())
            .content(post.getContents())
            .authorId(post.getAuthor().getId())
            .author(post.getAuthor().getNickname())
            .createdAt(post.getCreatedAt())
            .lastModifiedAt(post.getLastModifiedAt())
            .build();
    }

    public Page<PostResponse> getPostFollowingPage(Pageable pageable, Long currentId) {
        Page<Post> postPage = postRepository.findPostsByFollowings(currentId, pageable);

        return postPage.map(this::convertToResponse);
    }

    private void verifyAuthorOrThrow(long userId, User author) {
        if (isNotAuthor(userId, author)) {
            throw new AccessDeniedException();
        }
    }

    private boolean isNotAuthor(long userId, User author) {
        return !author.getId().equals(userId);
    }
}
