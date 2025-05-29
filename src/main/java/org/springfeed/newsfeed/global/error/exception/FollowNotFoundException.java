package org.springfeed.newsfeed.global.error.exception;

public class FollowNotFoundException extends RuntimeException {

    public FollowNotFoundException() {
        super("팔로우한 이력이 없습니다.");
    }

    public FollowNotFoundException(String message) {
        super(message);
    }
}
