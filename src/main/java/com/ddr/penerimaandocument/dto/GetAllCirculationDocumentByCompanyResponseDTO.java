package com.ddr.penerimaandocument.dto;

import lombok.Getter;
import lombok.Setter;
import com.ddr.penerimaandocument.model.CirculationDocument;

@Getter
@Setter
public class GetAllCirculationDocumentByCompanyResponseDTO {
    private CirculationDocument[] documents;
}
