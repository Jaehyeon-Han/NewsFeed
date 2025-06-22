package org.springfeed.newsfeed.global.jwt.blacklist;

// 어댑터 사용 또는 직접 구현
public interface BlacklistManager {

    /**
     * 블랙리스트에 remainingTimeInSecond 초만큼 남은 token을 추가한다.
     * @param token: JWT 토큰
     * @param remainingTimeInSecond: 남은 유효기간 (초)
     */
    void add(String token, long remainingTimeInSecond);

    /**
     * 블랙리스트 내에 토큰이 존재하는지 확인한다.
     * @param token: JWT 토큰
     * @return 블랙리스트 내 토큰의 존재 여부
     */
    boolean contains(String token);
}
