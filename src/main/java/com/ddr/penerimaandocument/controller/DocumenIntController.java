package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import com.ddr.penerimaandocument.dto.AddDocumentReqDTO;
import com.ddr.penerimaandocument.dto.DeleteDocumentDTO;
import com.ddr.penerimaandocument.dto.EditDocumentReqDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.DocumentType;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.service.DocumentService;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/document-in")
public class DocumenIntController {

    private final Integer CODE_MENU = 3;
    
   @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentService documentService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    VendorRepository vendorRepository;

    @GetMapping(path = "/")
    public String showDocumentOut(Model model){
        List<Document> data = documentRepository.findByType(DocumentType.IN);
        model.addAttribute("document", data);
        return "document/in";
    }

    @GetMapping("/add")
    public String addDocument(Model model) {
        String docId = documentRepository.findLastIn();

        if (docId == null){
            String counter = "DOC";
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            String lastTwoDigitsOfYear = sdf.format(currentDate);
            counter += lastTwoDigitsOfYear;
            counter += "0001";

            model.addAttribute("counter", counter);
        }
        else {
            String prefix = docId.substring(0, 3);
            String numericPart = docId.substring(5); // "0001"

            int incremented = Integer.parseInt(numericPart) + 1;

            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            String lastTwoDigitsOfYear = sdf.format(currentDate);
            prefix += lastTwoDigitsOfYear;
            String incrementedNumericPart = String.format("%04d", incremented);

            String newCompanyId = prefix + incrementedNumericPart;

            model.addAttribute("counter", newCompanyId);
        }
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("vendors", vendorRepository.getAllVendorName());
        model.addAttribute("type", "IN");
        return "document/add";
    }
    

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@RequestParam("addDocumentData[]") String[] data, @RequestParam("file") MultipartFile file) {

        AddDocumentReqDTO req = new AddDocumentReqDTO();
        req.setDocumentId(data[0]);
        req.setDescription(data[1]);
        req.setInvoiceRef(data[2]);
        req.setCompanyTo(data[3]);
        req.setVendor(data[4]);
        req.setContactName(data[5]);
        req.setContactNumber(data[6]);
        req.setCreatedBy(data[7]);
        req.setUpdatedBy(data[7]);
        req.setStatus(data[8]);
        req.setDocumentType(data[9]);
        req.setFile(file);
        
        ResponseEntity<?> res = documentService.addDocument(req);

        return res;
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editDocument(@RequestParam("editDocumentData[]") String[] data, @RequestParam(value="file", required = false) MultipartFile file) {

        EditDocumentReqDTO req = new EditDocumentReqDTO();
        req.setDocumentId(data[0]);
        req.setDescription(data[1]);
        req.setCompanyTo(data[2]);
        req.setVendor(data[3]);
        req.setContactName(data[4]);
        req.setContactNumber(data[5]);
        req.setUpdatedBy(data[6]);

        req.setFile(file);
        
        ResponseEntity<?> res = documentService.editDocument(req);

        return res;
    }

    @PostMapping("/delete")
    public String deleteDocument(@RequestBody DeleteDocumentDTO req) {
        documentService.delete(req);

        return "document/in";
    }

    @GetMapping("/{documentId}")
    public String getMethodName(@PathVariable("documentId") String documentId, Model model) {
        Document data = documentRepository.getReferenceById(documentId);
        model.addAttribute("doc", data);
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("vendors", vendorRepository.getAllVendorName());
        model.addAttribute("type", "IN");
        return "document/edit";
    }
}
