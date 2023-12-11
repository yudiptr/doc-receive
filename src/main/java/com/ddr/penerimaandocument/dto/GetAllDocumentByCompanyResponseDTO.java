package com.ddr.penerimaandocument.dto;

import lombok.Getter;
import lombok.Setter;
import com.ddr.penerimaandocument.model.Document;

@Getter
@Setter
public class GetAllDocumentByCompanyResponseDTO {
    private Document[] documents;
}
