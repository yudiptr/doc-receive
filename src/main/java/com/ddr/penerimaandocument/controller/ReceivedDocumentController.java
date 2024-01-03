package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.AddReceivedDocumentDTO;
import com.ddr.penerimaandocument.dto.DeleteReceivedDocumentDTO;
import com.ddr.penerimaandocument.dto.GetAllCirculationDocumentByCompanyResponseDTO;
import com.ddr.penerimaandocument.dto.UpdateReceivedDocumentDTO;
import com.ddr.penerimaandocument.model.CirculationDocument;
import com.ddr.penerimaandocument.model.ReceivedDocument;
import com.ddr.penerimaandocument.repository.CirculationDocumentRepository;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.repository.ReceivedDocumentRepository;
import com.ddr.penerimaandocument.service.JwtService;
import com.ddr.penerimaandocument.service.ReceivedDocumentService;
import com.ddr.penerimaandocument.service.UtilService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/received-document")
public class ReceivedDocumentController {
    
    private final Integer CODE_MENU = 9;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ReceivedDocumentRepository receivedDocumentRepository;

    @Autowired
    private ReceivedDocumentService receivedDocumentService;
    
    @Autowired
    private CirculationDocumentRepository circulationDocumentRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private JwtService jwtService;


    @GetMapping("/docs/{companyId}")
    public ResponseEntity<?> getDocsByCompany(HttpServletRequest request, @PathVariable("companyId") String Cid) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        GetAllCirculationDocumentByCompanyResponseDTO res = new GetAllCirculationDocumentByCompanyResponseDTO();
        List<CirculationDocument> data = circulationDocumentRepository.findByCompanySubmitted(Cid, "IN");
        res.setDocuments(data.stream().toArray(CirculationDocument[]::new));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{receiveId}")
    public String updateReceivedDocument(@PathVariable("receiveId") String receiveId, Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        ReceivedDocument data = receivedDocumentRepository.getReferenceById(receiveId);

        List<CirculationDocument> dataDocument = new ArrayList<>();
        List<CirculationDocument> dataAllDocument = circulationDocumentRepository.findByCompanySubmitted(data.getCompany().getCompanyId(),"IN");
 
        for (String i : data.getDocumentsId()){
            CirculationDocument temp = circulationDocumentRepository.getReferenceById(i);
            dataDocument.add(temp);
        }

        model.addAttribute("data", data);
        model.addAttribute("companies", companyRepository.getAllCompanyName());
        model.addAttribute("docs", dataDocument);
        model.addAttribute("allDocs", dataAllDocument);
        return "received_document/edit";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReceivedDocument(HttpServletRequest request, @RequestBody AddReceivedDocumentDTO req) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        HttpSession session = request.getSession();
        String username = jwtService.extractUsername((String) session.getAttribute("token"));

        receivedDocumentService.addReceivedDocument(req, username);
        return ResponseEntity.ok("Success Add Received Document");
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdateReceivedDocument(HttpServletRequest request, @RequestBody UpdateReceivedDocumentDTO req) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        receivedDocumentService.updateReceivedDocument(req);
        return ResponseEntity.ok("Success Update Received Document");
    }

    @GetMapping("/")
    public String showReceivedDocument(Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        List<ReceivedDocument> data = receivedDocumentRepository.findByIsClosed();

        model.addAttribute("received_document", data);
        return "received_document/list";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteReceivedDocument(HttpServletRequest request, @RequestBody DeleteReceivedDocumentDTO req) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        receivedDocumentService.delete(req);
        return ResponseEntity.ok("Delete Success");
    }
    
    
    @GetMapping("/add")
    public String addDocument(Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

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
