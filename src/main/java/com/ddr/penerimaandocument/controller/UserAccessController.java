package com.ddr.penerimaandocument.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ddr.penerimaandocument.dto.CreateUserDTO;
import com.ddr.penerimaandocument.dto.DeleteUserDTO;
import com.ddr.penerimaandocument.dto.GetUserResDTO;
import com.ddr.penerimaandocument.model.Role;
import com.ddr.penerimaandocument.model.User;
import com.ddr.penerimaandocument.repository.RoleRepository;
import com.ddr.penerimaandocument.repository.UserRepository;
import com.ddr.penerimaandocument.service.AuthService;
import com.ddr.penerimaandocument.service.UtilService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/user-access")
public class UserAccessController{
    private final Integer CODE_MENU = 7;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String getAllUser(Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";

        List<User> data = userRepository.findAll();
        List<Role> role = roleRepository.findAll();

        model.addAttribute("user", data);
        model.addAttribute("role", role);
        
        return "user_access/list";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        GetUserResDTO res = authService.getUserProfile(userId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(HttpServletRequest request, @RequestBody CreateUserDTO entity) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        authService.createUser(entity);
        return ResponseEntity.ok("Sukses Tambah User");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @RequestBody DeleteUserDTO entity) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        authService.deleteUser(entity);
        return ResponseEntity.ok("Sukses Delete User");
    }
    
    
}
