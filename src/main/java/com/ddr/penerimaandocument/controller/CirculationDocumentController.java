package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.AddCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.DeleteCirculationDocumentDTO;
import com.ddr.penerimaandocument.dto.GetAllDocumentByCompanyResponseDTO;
import com.ddr.penerimaandocument.dto.UpdateCirculationDocumentDTO;
import com.ddr.penerimaandocument.model.CirculationDocument;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.service.CirculationDocumentService;
import com.ddr.penerimaandocument.service.UtilService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/circulation-document")
public class CirculationDocumentController {

    private final Integer CODE_MENU = 8;

    @Autowired
    private CirculationDocumentService circulationDocumentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private CirculationDocumentRepository circulationDocumentRepository;


    @GetMapping("/docs/{companyId}")
    public ResponseEntity<?> getDocsByCompany(HttpServletRequest request, @PathVariable("companyId") String Cid) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        GetAllDocumentByCompanyResponseDTO res = new GetAllDocumentByCompanyResponseDTO();
        List<Document> data = documentRepository.findByCompanyDraft(Cid);
        res.setDocuments(data.stream().toArray(Document[]::new));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/edit/docs/{companyId}/{circlId}")
    @JsonIgnore
    public ResponseEntity<?> getEditDocsByCompany(HttpServletRequest request, @PathVariable("companyId") String Cid, @PathVariable("circlId") String cirId) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        GetAllDocumentByCompanyResponseDTO res = new GetAllDocumentByCompanyResponseDTO();
        List<Document> data = documentRepository.findByCompanyDraft(Cid);
        System.out.println(data);
        List<Document> data2 = documentRepository.findByCompanySubmitted(Cid);
        CirculationDocument data3 = circulationDocumentRepository.getReferenceById(cirId);

        String[] documentIds = data3.getDocumentsId();

        List<Document> filteredData2 = new ArrayList<>();
        for (Document doc : data2) {
            for (String documentId : documentIds) {
                if (doc.getDocumentId().equals(documentId)) {
                    filteredData2.add(doc);
                    break;
                }
            }
        }

        data.addAll(filteredData2);
        res.setDocuments(data.stream().toArray(Document[]::new));
        
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{circlDocId}")
    public String updateCirculationDocument(HttpServletRequest request, @PathVariable("circlDocId") String circlId, Model model) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        CirculationDocument data = circulationDocumentRepository.getReferenceById(circlId);

        List<Document> dataDocument = new ArrayList<>();
        List<Document> dataAllDocument = documentRepository.findByCompanyDraft(data.getCompany().getCompanyId());
 
        for (String i : data.getDocumentsId()){
            Document temp = documentRepository.getReferenceById(i);
            dataDocument.add(temp);
        }

        model.addAttribute("data", data);
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("docs", dataDocument);
        model.addAttribute("allDocs", dataAllDocument);
        return "circulation_document/edit";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCirculationDocument(@RequestBody AddCirculationDocumentDTO req, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        circulationDocumentService.addCirculationDocument(req);
        return ResponseEntity.ok("Success Add Circulation Document");
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdateCirculationDocument(@RequestBody UpdateCirculationDocumentDTO req, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        circulationDocumentService.updateCirculationDocument(req);
        return ResponseEntity.ok("Success Update Circulation Document");
    }

    @GetMapping("/")
    public String showCirculationDocument(Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        List<CirculationDocument> data = circulationDocumentRepository.findAll();

        model.addAttribute("circulation_document", data);
        return "circulation_document/list";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCirclDocument(@RequestBody DeleteCirculationDocumentDTO req, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        circulationDocumentService.delete(req);
        return ResponseEntity.ok("Delete Success");
    }
    
    
    @GetMapping("/add")
    public String addDocument(Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

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
