package org.springfeed.newsfeed.global.error.exception;

public class SelfFollowException extends RuntimeException {

    public SelfFollowException() {
        super("자기 자신은 팔로우할 수 없습니다.");
    }

    public SelfFollowException(String message) {
        super(message);
    }
}
