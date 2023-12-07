package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ddr.penerimaandocument.dto.AddDocumentReqDTO;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.DocumentType;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.repository.VendorRepository;
import com.ddr.penerimaandocument.service.CompanyService;
import com.ddr.penerimaandocument.service.DocumentService;
import com.ddr.penerimaandocument.service.VendorService;

import java.text.SimpleDateFormat;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;

@Controller
@RequestMapping("/document-in")
public class DocumenIntController {
    
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
            counter += 0001;

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
        String UPLOAD_DIR = "C:\\Users\\YudiSabri\\Desktop\\docReceive\\";

        AddDocumentReqDTO req = new AddDocumentReqDTO();
        req.setDocumentId(data[0]);
        req.setDescription(data[1]);
        req.setInvoiceRef(data[2]);
        req.setCompanyTo(data[3]);
        req.setVendor(data[4]);
        req.setContactName(data[5]);
        req.setContactNumber(data[6]);
        req.setCreatedBy(data[7]);
        req.setStatus(data[8]);
        req.setDocumentType(data[9]);
        req.setFile(file);
        
        if (req.getFile() == null) {
            return ResponseEntity.ok("File gak ada");
        }

        try {
            byte[] bytes = req.getFile().getBytes();
            Path path = Paths.get(UPLOAD_DIR + req.getFile().getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("SUKSES");
            return  ResponseEntity.ok("Sukses");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED");
            return  ResponseEntity.ok("Gagal");
        }
    }
    
}
