package com.ddr.penerimaandocument.dto;

import lombok.Getter;

@Getter
public class CreateUserDTO {
    private String username;
    private String password;
    private Integer role;
}
