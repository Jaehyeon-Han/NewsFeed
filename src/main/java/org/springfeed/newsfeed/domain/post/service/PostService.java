package org.springfeed.newsfeed.domain.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.post.dto.response.PostResponse;
import org.springfeed.newsfeed.domain.post.repository.PostRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse save(String title, String content, long userId) {

        Optional<User> findUser = userRepository.findById(userId);

        if(findUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User foundUser = findUser.get();

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
        // Todo
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
        // Todo
        Post foundPost = postRepository.findPostByIdOrElseThrow(postId);
        User author = foundPost.getAuthor();

        if(!foundPost.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

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
        // Todo
        Post foundPost = postRepository.findPostByIdOrElseThrow(postId);

        if(!foundPost.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        postRepository.delete(foundPost);
    }

    public Page<PostResponse> getPostPage(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.map(this::convertToResponse);
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
}
