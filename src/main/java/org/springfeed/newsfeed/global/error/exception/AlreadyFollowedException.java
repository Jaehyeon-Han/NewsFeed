package org.springfeed.newsfeed.global.error.exception;

public class AlreadyFollowedException extends RuntimeException {

    public AlreadyFollowedException() {
        super("이미 팔로우 중입니다.");
    }

    public AlreadyFollowedException(String message) {
        super(message);
    }
}
