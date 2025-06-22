package org.springfeed.newsfeed.global.jwt.blacklist;

public class RedisBlacklistManager implements BlacklistManager {

    @Override
    public void add(String token, long remainingTimeInSecond) {

    }

    @Override
    public boolean contains(String token) {
        return false;
    }
}
