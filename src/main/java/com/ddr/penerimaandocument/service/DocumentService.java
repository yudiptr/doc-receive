package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.model.Document;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

	@Autowired
	public DocumentService() {
	}

}
