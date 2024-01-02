package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.DeleteCompanyRequestDTO;
import com.ddr.penerimaandocument.dto.EditMasterCompanyDTO;
import com.ddr.penerimaandocument.dto.CreateMasterCompanyRequestDTO;
import com.ddr.penerimaandocument.model.Company;
import com.ddr.penerimaandocument.repository.CompanyRepository;
import com.ddr.penerimaandocument.service.CompanyService;
import com.ddr.penerimaandocument.service.UtilService;

import java.util.List;
import org.springframework.beans.factory.ObjectFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final Integer CODE_MENU = 2;
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;
    
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping(path = "/add")
    public String addCompany(Model model, HttpServletRequest request){
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";
        String cid = companyService.testGetData();

        if (cid == null){
            model.addAttribute("counter", "CID0001");
        }
        else {
            String prefix = cid.substring(0, 3);
            String numericPart = cid.substring(3); // "0001"

            int incremented = Integer.parseInt(numericPart) + 1;
            String incrementedNumericPart = String.format("%04d", incremented);

            String newCompanyId = prefix + incrementedNumericPart;

            model.addAttribute("counter", newCompanyId);
        }
        return "company/add";
    }

    @PostMapping("/add-company")
    public ResponseEntity<?> addCompanyPost(@RequestBody CreateMasterCompanyRequestDTO entity, HttpServletRequest request) {
         if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        companyService.addMasterCompany(entity);
        return ResponseEntity.ok("Add Success");
    }
    
    @GetMapping(path = "/")
    public String showCompany(Model model, HttpServletRequest request){
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";
        List<Company> data = companyService.getAllVendor();

        model.addAttribute("companies", data);
        return "company/list";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCompany(@RequestBody DeleteCompanyRequestDTO req, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        companyService.deleteCompany(req);
        return ResponseEntity.ok("Delete Success");
    }

    @GetMapping("/{companyId}")
    public String editCompany(@PathVariable("companyId") String Cid, Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        Company data = companyRepository.getReferenceById(Cid);
        model.addAttribute("company", data);
        return "company/edit";
    }
    
    @PostMapping("/edit-company")
    public ResponseEntity<?> editCompanyPost(@RequestBody EditMasterCompanyDTO entity, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        companyService.editMasterCompany(entity);
        return ResponseEntity.ok("Edit Success");
    }
    
}
