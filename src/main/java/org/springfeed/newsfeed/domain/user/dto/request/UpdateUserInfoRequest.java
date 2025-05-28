package org.springfeed.newsfeed.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserInfoRequest {

    private String nickname;
    private String introduction;

}
