package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.CreateMasterVendorRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteVendorRequestDTO;
import com.ddr.penerimaandocument.dto.EditMasterVendorDTO;
import com.ddr.penerimaandocument.model.Vendor;
import com.ddr.penerimaandocument.repository.VendorRepository;
import java.util.List;
import com.ddr.penerimaandocument.service.UtilService;
import com.ddr.penerimaandocument.service.VendorService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/vendor")
public class VendorController {

    private final Integer CODE_MENU = 1;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping(path = "/add")
    public String addVendor(Model model){

        String cid = vendorService.testGetData();

        if(cid == null){
            model.addAttribute("counter", "VID0001");
        }
        else {
            String prefix = cid.substring(0, 3);
            String numericPart = cid.substring(3); // "0001"

            int incremented = Integer.parseInt(numericPart) + 1;
            String incrementedNumericPart = String.format("%04d", incremented);

            String newVendorId = prefix + incrementedNumericPart;

            model.addAttribute("counter", newVendorId);
        }

        return "vendor/add";
    }

    @PostMapping(path = "/add-vendor")
    public ResponseEntity<?> addVendorPost(@RequestBody CreateMasterVendorRequestDTO entity){
        vendorService.addMasterVendor(entity);
        return ResponseEntity.ok("Add Success");
    }

    @GetMapping(path = "/")
    public String showVendor(Model model, HttpServletRequest request){
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        List<Vendor> data = vendorService.getAllVendor();

        model.addAttribute("vendors", data);
        return "vendor/list";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCompany(@RequestBody DeleteVendorRequestDTO req) {
        vendorService.deleteVendor(req);
        return ResponseEntity.ok("Delete Success");
    }

    @PostMapping("/edit-vendor")
    public ResponseEntity<?> edit(@RequestBody EditMasterVendorDTO entity) {
        vendorService.editMasterVendor(entity);
        return ResponseEntity.ok("Edit Success");
    }

    @GetMapping("/{vendorId}")
    public String editCompany(@PathVariable("vendorId") String Vid, Model model) {
        Vendor data = vendorRepository.getReferenceById(Vid);
        model.addAttribute("vendor", data);
        return "vendor/edit";
    }
}
