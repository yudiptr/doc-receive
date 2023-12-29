package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.ddr.penerimaandocument.dto.LoginRequestDTO;
import com.ddr.penerimaandocument.service.AuthService;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class GeneralController {
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    private AuthService authService;

    @GetMapping(path = "/")
    public String hello(Model model) throws IOException, InterruptedException{
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        HttpSession session = httpSessionFactory.getObject();
        String res = authService.doLogin(req);
        session.setAttribute("token", res);
        return ResponseEntity.ok("OK");
    }
}
