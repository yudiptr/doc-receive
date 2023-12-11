package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.AddCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.GetAllDocumentByCompanyResponseDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.service.CirculationDocumentService;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/circulation-document")
public class CirculationDocumentController {
    
   @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    CirculationDocumentService circulationDocumentService;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    VendorRepository vendorRepository;  

    @Autowired
    CirculationDocumentRepository circulationDocumentRepository;


    @GetMapping("/{companyId}")
    public ResponseEntity<GetAllDocumentByCompanyResponseDTO> getDocsByCompany(@PathVariable("companyId") String Cid) {
        GetAllDocumentByCompanyResponseDTO res = new GetAllDocumentByCompanyResponseDTO();
        List<Document> data = documentRepository.findByCompanyDraft(Cid);
        res.setDocuments(data.stream().toArray(Document[]::new));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCirculationDocument(@RequestBody AddCirculationDocumentDTO req) {

        circulationDocumentService.addCirculationDocument(req);
        return ResponseEntity.ok("Success Add Circulation Document");
    }
    
    

    @GetMapping("/add")
    public String addDocument(Model model) {
        String circId = circulationDocumentRepository.findLastIn();

        if (circId == null){
            String counter = "CRI";
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            String lastTwoDigitsOfYear = sdf.format(currentDate);
            counter += lastTwoDigitsOfYear;
            counter += "0001";

            model.addAttribute("counter", counter);
        }
        else {
            String prefix = circId.substring(0, 3);
            String numericPart = circId.substring(5); // "0001"

            int incremented = Integer.parseInt(numericPart) + 1;

            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            String lastTwoDigitsOfYear = sdf.format(currentDate);
            prefix += lastTwoDigitsOfYear;
            String incrementedNumericPart = String.format("%04d", incremented);

            String newCircId = prefix + incrementedNumericPart;

            model.addAttribute("counter", newCircId);
        }
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        return "circulation_document/add";
    }
    
}
