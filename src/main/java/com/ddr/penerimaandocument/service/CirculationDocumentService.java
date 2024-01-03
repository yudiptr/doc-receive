package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddr.penerimaandocument.dto.AddCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.DeleteCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.UpdateCirculationDocumentDTO;
import com.ddr.penerimaandocument.model.CirculationDocument;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.Status;
import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;

@Service
public class CirculationDocumentService {
    @Autowired
    private CirculationDocumentRepository circulationDocumentRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired DocumentRepository documentRepository;

	public CirculationDocumentService() {
	}

	public void addCirculationDocument(AddCirculationDocumentDTO req, String type, String user){
		CirculationDocument data = new CirculationDocument();
		data.setCirculationDocId(req.getCirculationDocumentId());
		data.setClosed(false);
		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());
		data.setCreatedBy(user); // to be changed, by on logged in user
		data.setType(type);

		for (String i : req.getDocumentsId()){
			Document doc = documentRepository.getReferenceById(i);
			doc.setStatus(Status.SUBMITTED);
			documentRepository.save(doc);
		}
		circulationDocumentRepository.save(data);
	}

	public void updateCirculationDocument(UpdateCirculationDocumentDTO req){
		CirculationDocument data = circulationDocumentRepository.getReferenceById(req.getCirculationDocumentId());

		for (String i : data.getDocumentsId()){
			Document doc = documentRepository.getReferenceById(i);
			doc.setStatus(Status.DRAFT);
			documentRepository.save(doc);
		}

		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());

		for (String i : req.getDocumentsId()){
			Document doc = documentRepository.getReferenceById(i);
			doc.setStatus(Status.SUBMITTED);
			documentRepository.save(doc);
		}
		
		circulationDocumentRepository.save(data);
	}

	public void delete(DeleteCirculationDocumentDTO req){

		for (String i : req.getCirculationsValue()){
			CirculationDocument data = circulationDocumentRepository.getReferenceById(i);
			String[] dataDoc = data.getDocumentsId();

			for (String j : dataDoc){
				Document temp = documentRepository.getReferenceById(j);
				temp.setStatus(Status.DRAFT);
				documentRepository.save(temp);
			}
			
			circulationDocumentRepository.deleteById(i);
		}
	}
}
