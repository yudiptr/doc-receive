package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.hibernate.Hibernate;
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
import com.ddr.penerimaandocument.model.DocumentType;
import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.service.CirculationDocumentService;
import com.ddr.penerimaandocument.service.GeneratePDFService;
import com.ddr.penerimaandocument.service.JwtService;
import com.ddr.penerimaandocument.service.UtilService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/circulation-document-out")
public class CirculationDocumentOutController {

    private final Integer CODE_MENU = 10;

    @Autowired
    private CirculationDocumentService circulationDocumentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private GeneratePDFService pdfService;

    @Autowired
    private CirculationDocumentRepository circulationDocumentRepository;


    @GetMapping("/docs/{companyId}")
    public ResponseEntity<?> getDocsByCompany(HttpServletRequest request, @PathVariable("companyId") String Cid) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        GetAllDocumentByCompanyResponseDTO res = new GetAllDocumentByCompanyResponseDTO();
        List<Document> data = documentRepository.findByCompanyDraft(Cid, DocumentType.OUT);
        res.setDocuments(data.stream().toArray(Document[]::new));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/edit/docs/{companyId}/{circlId}")
    @JsonIgnore
    public ResponseEntity<?> getEditDocsByCompany(HttpServletRequest request, @PathVariable("companyId") String Cid, @PathVariable("circlId") String cirId) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        GetAllDocumentByCompanyResponseDTO res = new GetAllDocumentByCompanyResponseDTO();
        List<Document> data = documentRepository.findByCompanyDraft(Cid, DocumentType.OUT);
        List<Document> data2 = documentRepository.findByCompanySubmitted(Cid, DocumentType.OUT);
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
        List<Document> dataAllDocument = documentRepository.findByCompanyDraft(data.getCompany().getCompanyId(), DocumentType.OUT);
 
        for (String i : data.getDocumentsId()){
            Document temp = documentRepository.getReferenceById(i);
            dataDocument.add(temp);
        }

        model.addAttribute("data", data);
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("docs", dataDocument);
        model.addAttribute("allDocs", dataAllDocument);
        return "circulation_document_out/edit";
    }

    @GetMapping("/get/{circlDocId}")
    public ResponseEntity<?> getData(@PathVariable("circlDocId") String circlId, HttpServletResponse response) {
        CirculationDocument circulationDocument = circulationDocumentRepository.getReferenceById(circlId);
        circulationDocument = (CirculationDocument) Hibernate.unproxy(circulationDocument);

        if (circulationDocument != null) {
            List<Document> dataDocs = new ArrayList<Document>();
            for (String i : circulationDocument.getDocumentsId()){
                Document docs = documentRepository.getReferenceById(i);
                docs = (Document) Hibernate.unproxy(docs);
                dataDocs.add(docs);
            }

            Map<String, Object> temp = new HashMap<>();
            temp.put("circl", circulationDocument);
            temp.put("docs", dataDocs);

            pdfService.generatePdfFile("circulationDocumentPDF", temp, circlId + ".pdf", response);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    

    @PostMapping("/add")
    public ResponseEntity<?> addCirculationDocument(@RequestBody AddCirculationDocumentDTO req, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        HttpSession session = request.getSession();
        String username = jwtService.extractUsername((String) session.getAttribute("token"));
        circulationDocumentService.addCirculationDocument(req, "OUT", username);
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

        List<CirculationDocument> data = circulationDocumentRepository.findByType("OUT");

        model.addAttribute("circulation_document", data);
        return "circulation_document_out/list";
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
        return "circulation_document_out/add";
    }
    
}
