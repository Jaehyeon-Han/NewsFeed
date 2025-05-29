package org.springfeed.newsfeed.global.error.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("권한이 없습니다.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
