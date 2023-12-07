package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.ddr.penerimaandocument.repository.DocumentRepository;
import com.ddr.penerimaandocument.service.DocumentService;
import com.ddr.penerimaandocument.model.Document;
import com.ddr.penerimaandocument.model.DocumentType;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/document-out")
public class DocumentOutController {
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentService documentService;

    @GetMapping(path = "/")
    public String showDocumentOut(Model model){
        
        List<Document> data = documentRepository.findByType(DocumentType.OUT);
        model.addAttribute("document", data);
        return "document/out";
    }
    
}
