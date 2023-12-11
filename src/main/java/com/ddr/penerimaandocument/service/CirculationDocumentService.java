package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ddr.penerimaandocument.dto.AddCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.CreateMasterCompanyRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteCompanyRequestDTO;
import com.ddr.penerimaandocument.dto.EditMasterCompanyDTO;
import com.ddr.penerimaandocument.model.CirculationDocument;
import com.ddr.penerimaandocument.model.Company;
import java.util.List;

import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;

@Service
public class CirculationDocumentService {
    
    private final RestTemplate restTemplate;

    @Autowired
    private CirculationDocumentRepository circulationDocumentRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public CirculationDocumentService(@Qualifier("restTemplateHttp")RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void addCirculationDocument(AddCirculationDocumentDTO req){
		CirculationDocument data = new CirculationDocument();
		data.setCirculationDocId(req.getCirculationDocumentId());
		data.setClosed(false);
		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());

		circulationDocumentRepository.save(data);
	}
}
