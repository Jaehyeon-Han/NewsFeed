package org.springfeed.newsfeed.global.error.exception;

public class PasswordUnchangedException extends RuntimeException {

  public PasswordUnchangedException() {
    super("새 비밀번호는 현재 비밀번호와 같을 수 없습니다.");
  }

  public PasswordUnchangedException(String message) {
        super(message);
    }
}
