package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddr.penerimaandocument.dto.AddCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.DeleteCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.UpdateCirculationDocumentDTO;
import com.ddr.penerimaandocument.model.CirculationDocument;

import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;

@Service
public class CirculationDocumentService {
    @Autowired
    private CirculationDocumentRepository circulationDocumentRepository;

	@Autowired
	private CompanyRepository companyRepository;

	public CirculationDocumentService() {
	}

	public void addCirculationDocument(AddCirculationDocumentDTO req){
		CirculationDocument data = new CirculationDocument();
		data.setCirculationDocId(req.getCirculationDocumentId());
		data.setClosed(false);
		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());
		data.setCreatedBy(""); // to be changed, by on logged in user

		circulationDocumentRepository.save(data);
	}

	public void updateCirculationDocument(UpdateCirculationDocumentDTO req){
		CirculationDocument data = circulationDocumentRepository.getReferenceById(req.getCirculationDocumentId());
		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());
		
		circulationDocumentRepository.save(data);
	}

	public void delete(DeleteCirculationDocumentDTO req){

		for (String i : req.getCirculationsValue()){
			circulationDocumentRepository.deleteById(i);
		}
	}
}
