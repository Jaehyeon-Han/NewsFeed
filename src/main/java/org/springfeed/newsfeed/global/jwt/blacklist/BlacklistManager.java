package org.springfeed.newsfeed.global.jwt.blacklist;

// 어댑터 사용 또는 직접 구현
public interface BlacklistManager {
    void add(String token, long remainingTime);

    boolean contains(String token);
}
