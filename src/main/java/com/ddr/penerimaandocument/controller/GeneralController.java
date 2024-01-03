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
import com.ddr.penerimaandocument.service.JwtService;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GeneralController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @GetMapping(path = "/")
    public String hello() throws IOException, InterruptedException{
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("token");
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String res = authService.doLogin(req);
        Integer roleId = Integer.parseInt(jwtService.extractRoleFromToken(res));

        List<String> menuRole = authService.getAllRole(roleId);
        System.out.println(menuRole);
        session.setAttribute("token", res);
        session.setAttribute("role", menuRole);

        return ResponseEntity.ok("OK");
    }
}
