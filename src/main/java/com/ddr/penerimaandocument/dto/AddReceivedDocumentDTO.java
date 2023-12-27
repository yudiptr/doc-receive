package com.ddr.penerimaandocument.dto;

import lombok.Getter;

@Getter

public class AddReceivedDocumentDTO {
    private String receivedDocumentId;
    private String[] documentsId;
    private String companyTo;
    private String receivedBy;
    private String refDescription;
}
