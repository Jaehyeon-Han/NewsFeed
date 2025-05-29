package org.springfeed.newsfeed.domain.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.domain.post.dto.response.PostResponse;
import org.springfeed.newsfeed.domain.post.repository.PostRepository;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse save(String title, String content, long userId) {

        User foundUser = userRepository.findUserByIdOrElseThrow(userId);

        Post post = new Post(title, content);
        post.setUser(foundUser);

        Post createdPost = postRepository.save(post);

        return new PostResponse(
                createdPost.getId(),
                createdPost.getTitle(),
                createdPost.getContent(),
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
                foundPost.getContent(),
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
        foundPost.setContent(content);

        postRepository.save(foundPost);

        return new PostResponse(
                foundPost.getId(),
                foundPost.getTitle(),
                foundPost.getContent(),
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
}
