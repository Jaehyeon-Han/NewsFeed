package org.springfeed.newsfeed.global.error.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException() {
        super("존재하는 이메일입니다.");
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
