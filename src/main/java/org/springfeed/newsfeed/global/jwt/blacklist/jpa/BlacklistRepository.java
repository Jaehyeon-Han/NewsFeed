package org.springfeed.newsfeed.global.jwt.blacklist.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    boolean existsByToken(String token);
}
