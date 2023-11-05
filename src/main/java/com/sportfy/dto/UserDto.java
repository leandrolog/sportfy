package com.sportfy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String name;
    private String email;
    private Long id;
    private String role;
    private String password;

}
