package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddr.penerimaandocument.service.UtilService;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/vendor")
public class VendorController {
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
	private UtilService utilService;

    @GetMapping(path = "/add")
    public String hello(Model model){
        return "vendor/add";
    }
    
}
