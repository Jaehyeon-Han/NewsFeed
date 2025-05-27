package org.springfeed.newsfeed.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // Todo
}
