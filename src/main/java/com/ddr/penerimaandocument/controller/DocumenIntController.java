package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import com.ddr.penerimaandocument.dto.AddDocumentReqDTO;
import com.ddr.penerimaandocument.dto.DeleteDocumentDTO;
import com.ddr.penerimaandocument.dto.DownloadDocumentDTO;
import com.ddr.penerimaandocument.dto.EditDocumentReqDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.DocumentType;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.service.DocumentService;
import com.ddr.penerimaandocument.service.UtilService;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestBody;
import java.nio.file.Files;

@Controller
@RequestMapping("/document-in")
public class DocumenIntController {

    private final Integer CODE_MENU = 3;

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

    @PostMapping("/download")
    public ResponseEntity<Resource> downloadDocument(@RequestBody DownloadDocumentDTO req ) throws IOException {
        String UPLOAD_DIR = "C:\\Users\\YudiSabri\\Desktop\\docReceive\\" + req.getFilePath();
        System.out.println("MASUK");
        System.out.println(UPLOAD_DIR);
        Path file = Paths.get(UPLOAD_DIR);
        Resource resource;
        try {
            resource = new org.springframework.core.io.ByteArrayResource(Files.readAllBytes(file));
        } catch (IOException e) {
            // Handle file not found or other exceptions
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Set the content type and disposition headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", req.getFilePath()); // Replace with your desired filename

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping(path = "/")
    public String showDocumentOut(Model model, HttpServletRequest request){
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        List<Document> data = documentRepository.findByType(DocumentType.IN);
        model.addAttribute("document", data);
        return "document/in";
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
        model.addAttribute("type", "IN");
        return "document/add";
    }
    

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@RequestParam("addDocumentData[]") String[] data, @RequestParam("file") MultipartFile file, HttpServletRequest request) {

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
    public ResponseEntity<?> editDocument(@RequestParam("editDocumentData[]") String[] data, @RequestParam(value="file", required = false) MultipartFile file, HttpServletRequest request) {

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
        model.addAttribute("type", "IN");
        return "document/edit";
    }
}
