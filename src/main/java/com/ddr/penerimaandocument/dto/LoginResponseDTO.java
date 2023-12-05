package com.ddr.penerimaandocument.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String username;
    private String defaultCompany;
    private String token;
    private String displayName;
    private String initial;
    private String companyName;
}
