package org.springfeed.newsfeed.domain.user.repository;

import org.springframework.stereotype.Repository;

@Repository
// 다른 사람 구현 전에 돌릴 수 있게 하기 위해 아무 기능 없는 리포지토리 주입
// 구현 완료 후 삭제 요망
public class FakeUserRepository implements UserRepository {

}
