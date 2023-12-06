package com.ddr.penerimaandocument.dto;

import lombok.Getter;

@Getter
public class CreateMasterVendorRequestDTO {
    private String vendorId;
    private String vendorName;
    private String vendorAddress;
    private String contactName;
    private String contactNumber;

}
