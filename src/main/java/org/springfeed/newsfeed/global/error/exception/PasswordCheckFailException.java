package org.springfeed.newsfeed.global.error.exception;

public class PasswordCheckFailException extends RuntimeException {

    public PasswordCheckFailException() {
        super("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    public PasswordCheckFailException(String message) {
        super(message);
    }
}
