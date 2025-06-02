package org.springfeed.newsfeed.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

// 회원가입 시 들어올 정보
@Getter
public class SignUpRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$", message = "비밀번호 형식이 올바르지 않습니다. 8자 이상, 대소문자 포함, 숫자 및 특수문자(@$!%*?&#) 포함")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력하세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$", message = "비밀번호 형식이 올바르지 않습니다. 8자 이상, 대소문자 포함, 숫자 및 특수문자(@$!%*?&#) 포함")
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력하세요.")
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    private String nickname;

    @Size(max = 500, message = "소개글은 500자 이하로 입력해주세요.")
    private String introduction = "";   //디폴트값 설정
}
