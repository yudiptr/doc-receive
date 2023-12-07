package com.ddr.penerimaandocument.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDocumentReqDTO implements Serializable {
    private String documentId;
    private String invoiceRef;
    private String description;
    private String companyTo;
    private String vendor;
    private String contactName;
    private String contactNumber;
    private MultipartFile file;
    private String createdBy;
    private String status;
    private String documentType;
}
