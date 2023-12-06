package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddr.penerimaandocument.dto.CreateMasterCompanyRequestDTO;
import com.ddr.penerimaandocument.dto.CreateMasterVendorRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteCompanyRequestDTO;
import com.ddr.penerimaandocument.model.Vendor;
import java.util.List;
import com.ddr.penerimaandocument.service.UtilService;
import com.ddr.penerimaandocument.service.VendorService;

import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/vendor")
public class VendorController {
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
	private UtilService utilService;

    @Autowired
    private VendorService vendorService;

    @GetMapping(path = "/add")
    public String addVendor(Model model){

        String cid = vendorService.testGetData();
     
        String prefix = cid.substring(0, 4);
        String numericPart = cid.substring(5); // "0001"

        int incremented = Integer.parseInt(numericPart) + 1;
        String incrementedNumericPart = String.format("%04d", incremented);

        String newVendorId = prefix + incrementedNumericPart;

        model.addAttribute("counter", newVendorId);

        return "vendor/add";
    }

    @PostMapping(path = "/add-vendor")
    public ResponseEntity<?> addVendorPost(@RequestBody CreateMasterVendorRequestDTO entity){
        vendorService.addMasterVendor(entity);
        return ResponseEntity.ok("Add Success");
    }

    @GetMapping(path = "/")
    public String showVendor(Model model){
        List<Vendor> data = vendorService.getAllVendor();

        model.addAttribute("vendors", data);
        return "vendor/list";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCompany(@RequestBody DeleteCompanyRequestDTO req) {
        
        return ResponseEntity.ok("Delete Success");
    }
    
}
