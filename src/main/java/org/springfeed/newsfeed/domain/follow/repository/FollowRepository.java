package org.springfeed.newsfeed.domain.follow.repository;

import org.springfeed.newsfeed.domain.entity.Follow;
import org.springfeed.newsfeed.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 이미 팔로우했는지 여부 확인
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 특정 사용자 간의 팔로우 관계 조회
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // followerId 사용자가 팔로우한 모든 목록 조회 (내가 팔로우한 사람들)
    List<Follow> findByFollowerId(Long followerId);

    // followingId 사용자를 팔로우한 모든 목록 조회 (나를 팔로우한 사람들)
    List<Follow> findByFollowingId(Long followingId);
}
