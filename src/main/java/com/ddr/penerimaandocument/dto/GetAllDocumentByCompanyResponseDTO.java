package com.ddr.penerimaandocument.dto;

import lombok.Getter;
import lombok.Setter;
import com.ddr.penerimaandocument.model.Document;

import io.micrometer.common.lang.Nullable;

@Getter
@Setter
public class GetAllDocumentByCompanyResponseDTO {
    private Document[] documents;

    @Nullable
    private Document[] selfDocumets;
}
