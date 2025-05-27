package org.springfeed.newsfeed;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springfeed.newsfeed.domain.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ConnectionTest {

    private static final Logger log = LoggerFactory.getLogger(ConnectionTest.class);

    @Autowired
    TestPostRepository postRepository;

    @Test
    void shouldConnect_withDefaultConfig () {
        // given
        Post post = new Post();

        // when
        Post save = postRepository.save(post);

        // then
        log.info(String.valueOf(save.getId()));
    }
}
