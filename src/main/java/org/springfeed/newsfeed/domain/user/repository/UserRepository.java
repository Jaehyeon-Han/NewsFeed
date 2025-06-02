package org.springfeed.newsfeed.domain.user.repository;

import org.springfeed.newsfeed.domain.entity.User;
import org.springfeed.newsfeed.global.error.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(UserNotFoundException::new);
    }
}