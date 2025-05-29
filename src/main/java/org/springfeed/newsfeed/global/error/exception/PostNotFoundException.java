package org.springfeed.newsfeed.global.error.exception;

public class PostNotFoundException extends  RuntimeException {

    public PostNotFoundException() {
        super("게시글이 없습니다.");
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
