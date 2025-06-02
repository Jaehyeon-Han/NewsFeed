package org.springfeed.newsfeed.domain.follow.repository;

import org.springfeed.newsfeed.domain.entity.Follow;
import org.springfeed.newsfeed.global.error.exception.FollowNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 이미 팔로우했는지 여부 확인
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 특정 사용자 간의 팔로우 관계 조회
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    default Follow findByFollowerIdAndFollowingIdOrElseThrow(Long followerId, Long followingId) {
        return findByFollowerIdAndFollowingId(followerId, followingId).orElseThrow(FollowNotFoundException::new);
    }

    @Query("""
        select f from Follow f
        join fetch  f.following
        where f.follower.id = :userId  
    """)
    List<Follow> findByFollowerIdWithFollowing(@Param("userId") Long userId);


    @Query("""
        select f from Follow f
        join fetch f.follower
        where f.following.id = :userId
    """)
    List<Follow> findByFollowingWithFollower(@Param("userId") Long userId);
}

