package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.AddReceivedDocumentDTO;
import com.ddr.penerimaandocument.dto.DeleteReceivedDocumentDTO;
import com.ddr.penerimaandocument.dto.GetAllDocumentByCompanyResponseDTO;
import com.ddr.penerimaandocument.dto.UpdateReceivedDocumentDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.ReceivedDocument;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.repository.ReceivedDocumentRepository;
import com.ddr.penerimaandocument.service.ReceivedDocumentService;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/received-document")
public class ReceivedDocumentController {
    
   @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    VendorRepository vendorRepository;  

    @Autowired
    ReceivedDocumentRepository receivedDocumentRepository;

    @Autowired
    ReceivedDocumentService receivedDocumentService;


    @GetMapping("/docs/{companyId}")
    public ResponseEntity<GetAllDocumentByCompanyResponseDTO> getDocsByCompany(@PathVariable("companyId") String Cid) {
        GetAllDocumentByCompanyResponseDTO res = new GetAllDocumentByCompanyResponseDTO();
        List<Document> data = documentRepository.findByCompanySubmitted(Cid);
        res.setDocuments(data.stream().toArray(Document[]::new));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{receiveId}")
    public String updateReceivedDocument(@PathVariable("receiveId") String receiveId, Model model) {
        ReceivedDocument data = receivedDocumentRepository.getReferenceById(receiveId);

        List<Document> dataDocument = new ArrayList<>();
        List<Document> dataAllDocument = documentRepository.findByCompanySubmitted(data.getCompany().getCompanyId());
 
        for (String i : data.getDocumentsId()){
            Document temp = documentRepository.getReferenceById(i);
            dataDocument.add(temp);
        }

        model.addAttribute("data", data);
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("docs", dataDocument);
        model.addAttribute("allDocs", dataAllDocument);
        return "received_document/edit";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReceivedDocument(@RequestBody AddReceivedDocumentDTO req) {
        receivedDocumentService.addReceivedDocument(req);
        return ResponseEntity.ok("Success Add Received Document");
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdateReceivedDocument(@RequestBody UpdateReceivedDocumentDTO req) {
        receivedDocumentService.updateReceivedDocument(req);
        return ResponseEntity.ok("Success Update Received Document");
    }

    @GetMapping("/")
    public String showReceivedDocument(Model model) {
        List<ReceivedDocument> data = receivedDocumentRepository.findByIsClosed();

        model.addAttribute("received_document", data);
        return "received_document/list";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteReceivedDocument(@RequestBody DeleteReceivedDocumentDTO req) {
        receivedDocumentService.delete(req);
        return ResponseEntity.ok("Delete Success");
    }
    
    
    @GetMapping("/add")
    public String addDocument(Model model) {
        String receivedId = receivedDocumentRepository.findLastIn();

        if (receivedId == null){
            String counter = "RCV";
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            String lastTwoDigitsOfYear = sdf.format(currentDate);
            counter += lastTwoDigitsOfYear;
            counter += "0001";

            model.addAttribute("counter", counter);
        }
        else {
            String prefix = receivedId.substring(0, 3);
            String numericPart = receivedId.substring(5); // "0001"

            int incremented = Integer.parseInt(numericPart) + 1;

            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            String lastTwoDigitsOfYear = sdf.format(currentDate);
            prefix += lastTwoDigitsOfYear;
            String incrementedNumericPart = String.format("%04d", incremented);

            String newReceivedId = prefix + incrementedNumericPart;

            model.addAttribute("counter", newReceivedId);
        }
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        return "received_document/add";
    }
    
}
