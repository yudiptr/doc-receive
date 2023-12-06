package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

import com.ddr.penerimaandocument.dto.LoginRequestDTO;
import com.ddr.penerimaandocument.dto.LoginResponseDTO;
import com.ddr.penerimaandocument.service.UtilService;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;

@Controller
public class GeneralController {
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
	private UtilService utilService;

    @GetMapping(path = "/")
    public String hello(Model model){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDTO req) {
        HttpSession session = httpSessionFactory.getObject();

        LinkedHashMap<String, Object> dataUser = new LinkedHashMap<String, Object>();
        dataUser.put("username", req.getUsername());
        dataUser.put("password", req.getPassword());
        

        LoginResponseDTO res =utilService.authenticate(dataUser);
        session.setAttribute("token", res.getToken());
        return "redirect:/";
    }
    
}
