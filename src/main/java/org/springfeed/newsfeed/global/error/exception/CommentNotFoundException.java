package org.springfeed.newsfeed.global.error.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("댓글이 없습니다.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
