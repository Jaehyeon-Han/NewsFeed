package org.springfeed.newsfeed.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springfeed.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Todo
}
