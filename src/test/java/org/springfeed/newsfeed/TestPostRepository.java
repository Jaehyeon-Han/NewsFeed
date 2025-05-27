package org.springfeed.newsfeed;

import org.springfeed.newsfeed.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPostRepository extends JpaRepository<Post, Long> {

}
