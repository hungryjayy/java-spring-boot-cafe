package com.kakao.cafe.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class UserUpdateRequest {

    public String password;
    public String name;
    public String email;
}
