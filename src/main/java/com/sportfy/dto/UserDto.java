package com.sportfy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {

    private String name;
    private String email;
    private Long id;
    private String gender;
    private String city;
    private Date birthDate;
    private String favoriteSport;
    private String role;
    private String password;

}
