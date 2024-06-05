package com.codeacademy.showcase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginRequestDTO {

    private String username;

    private String password;

}
