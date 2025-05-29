package org.springfeed.newsfeed.domain.user.repository;

import org.springfeed.newsfeed.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    default User findUserByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
