package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddr.penerimaandocument.dto.AddReceivedDocumentDTO;
import com.ddr.penerimaandocument.dto.DeleteReceivedDocumentDTO;
import com.ddr.penerimaandocument.dto.UpdateReceivedDocumentDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.ReceivedDocument;
import com.ddr.penerimaandocument.model.Status;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.ReceivedDocumentRepository;

@Service
public class ReceivedDocumentService {
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired DocumentRepository documentRepository;

    @Autowired
    private ReceivedDocumentRepository receivedDocumentRepository;

	public ReceivedDocumentService() {
	}

	public void addReceivedDocument(AddReceivedDocumentDTO req){
		ReceivedDocument data = new ReceivedDocument();
		data.setReceivedDocumentId(req.getReceivedDocumentId());
		data.setClosed(false);
		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());
		data.setCreatedBy(""); // to be changed, by on logged in user

		for (String i : req.getDocumentsId()){
			Document doc = documentRepository.getReferenceById(i);
			doc.setStatus(Status.RECEIVED);
			documentRepository.save(doc);
		}
		receivedDocumentRepository.save(data);
	}

	public void updateReceivedDocument(UpdateReceivedDocumentDTO req){
		ReceivedDocument data = receivedDocumentRepository.getReferenceById(req.getReceivedDocumentId());

		for (String i : data.getDocumentsId()){
			Document doc = documentRepository.getReferenceById(i);
			doc.setStatus(Status.SUBMITTED);
			documentRepository.save(doc);
		}

		data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
		data.setDocumentsId(req.getDocumentsId());
		data.setReceivedBy(req.getReceivedBy());
		data.setRefDescription(req.getRefDescription());

		for (String i : req.getDocumentsId()){
			Document doc = documentRepository.getReferenceById(i);
			doc.setStatus(Status.RECEIVED);
			documentRepository.save(doc);
		}
		
		receivedDocumentRepository.save(data);
	}

	public void delete(DeleteReceivedDocumentDTO req){

		for (String i : req.getReceivedValue()){
			receivedDocumentRepository.deleteById(i);
		}
	}
}
