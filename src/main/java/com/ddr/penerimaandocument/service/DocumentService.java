package com.ddr.penerimaandocument.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.dto.AddDocumentReqDTO;
import com.ddr.penerimaandocument.dto.DeleteDocumentDTO;
import com.ddr.penerimaandocument.dto.EditDocumentReqDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.DocumentType;
import com.ddr.penerimaandocument.model.Status;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired 
	VendorRepository vendorRepository;

	public ResponseEntity<?> addDocument(AddDocumentReqDTO req, String user){

		String UPLOAD_DIR = "C:\\Users\\YudiSabri\\Desktop\\docReceive\\";

		if (req.getFile() == null) {
            return ResponseEntity.ok("File gak ada");
        }

        try {
            byte[] bytes = req.getFile().getBytes();
            Path path = Paths.get(UPLOAD_DIR + req.getFile().getOriginalFilename());
            Files.write(path, bytes);

			Document data = new Document();
			data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
			data.setVendor(vendorRepository.getReferenceById(req.getVendor()));
			data.setDocumentId(req.getDocumentId());
			data.setInvoiceRef(req.getInvoiceRef());
			data.setDescription(req.getDescription());
			data.setContactName(req.getContactName());
			data.setContactNumber(req.getContactNumber());
			data.setDocumentPath(req.getFile().getOriginalFilename());
			data.setUpdatedBy(req.getUpdatedBy());
			data.setCreatedBy(user);
			data.setStatus(Status.DRAFT);
			data.setType(req.getDocumentType().equals("in") ? DocumentType.IN : DocumentType.OUT);

			documentRepository.save(data);

            return  ResponseEntity.ok("Sukses");
        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseEntity.ok("Gagal");
        }
	}

	public void delete(DeleteDocumentDTO req){
		for (String i : req.getDocumentValue()){
			documentRepository.deleteById(i);
		}
	} 

	public ResponseEntity<?> editDocument(EditDocumentReqDTO req){

		String UPLOAD_DIR = "C:\\Users\\YudiSabri\\Desktop\\docReceive\\";

        try {
			Document data = documentRepository.getReferenceById(req.getDocumentId());
			data.setCompany(companyRepository.getReferenceById(req.getCompanyTo()));
			data.setVendor(vendorRepository.getReferenceById(req.getVendor()));
			data.setDescription(req.getDescription());
			data.setContactName(req.getContactName());
			data.setContactNumber(req.getContactNumber());
			data.setUpdatedBy(req.getUpdatedBy());

			if(req.getFile() != null){
				byte[] bytes = req.getFile().getBytes();
				Path path = Paths.get(UPLOAD_DIR + req.getFile().getOriginalFilename());
				Files.write(path, bytes);
				data.setDocumentPath(req.getFile().getOriginalFilename());
			}

			documentRepository.save(data);

            return  ResponseEntity.ok("Sukses");
        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseEntity.ok("Gagal");
        }
	}

}
