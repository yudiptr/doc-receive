package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.service.DocumentService;
import com.ddr.penerimaandocument.service.UtilService;

import jakarta.servlet.http.HttpServletRequest;

import com.ddr.penerimaandocument.dto.AddDocumentReqDTO;
import com.ddr.penerimaandocument.dto.DeleteDocumentDTO;
import com.ddr.penerimaandocument.dto.EditDocumentReqDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.DocumentType;

@Controller
@RequestMapping("/document-out")
public class DocumentOutController {
    
    private final Integer CODE_MENU = 4;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UtilService utilService;

    @GetMapping(path = "/")
    public String showDocumentOut(Model model, HttpServletRequest request){
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        
        List<Document> data = documentRepository.findByType(DocumentType.OUT);
        model.addAttribute("document", data);
        return "document/out";
    }

    @GetMapping("/add")
    public String addDocument(Model model, HttpServletRequest request) {

        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

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
        model.addAttribute("type", "OUT");
        return "document/add";
    }
    

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(HttpServletRequest request, @RequestParam("addDocumentData[]") String[] data, @RequestParam("file") MultipartFile file) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
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
    public ResponseEntity<?> editDocument(@RequestParam("editDocumentData[]") String[] data,HttpServletRequest request, @RequestParam(value="file", required = false) MultipartFile file) {

        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
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
    public String deleteDocument(@RequestBody DeleteDocumentDTO req, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        documentService.delete(req);

        return "document/in";
    }

    @GetMapping("/{documentId}")
    public String getMethodName(@PathVariable("documentId") String documentId, Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        Document data = documentRepository.getReferenceById(documentId);
        model.addAttribute("doc", data);
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("vendors", vendorRepository.getAllVendorName());
        model.addAttribute("type", "OUT");
        return "document/edit";
    }
    
}
