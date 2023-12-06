package com.ddr.penerimaandocument.dto;

import lombok.Getter;

@Getter
public class CreateMasterCompanyRequestDTO {
    private String companyId;
    private String companyName;
    private String companyAddress;
    private String contactName;
    private String contactNumber;

}
