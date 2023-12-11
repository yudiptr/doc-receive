package com.ddr.penerimaandocument.dto;

import lombok.Getter;

@Getter

public class UpdateCirculationDocumentDTO {
    private String circulationDocumentId;
    private String[] documentsId;
    private String companyTo;
    private String receivedBy;
    private String refDescription;
}
