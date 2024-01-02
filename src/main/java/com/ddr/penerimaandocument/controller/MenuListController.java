package com.ddr.penerimaandocument.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ddr.penerimaandocument.dto.CreateMenuListRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteMenuListReqDTO;
import com.ddr.penerimaandocument.model.Role;
import com.ddr.penerimaandocument.model.UAM;
import com.ddr.penerimaandocument.model.Uam_Role_Join;
import com.ddr.penerimaandocument.repository.RoleRepository;
import com.ddr.penerimaandocument.repository.UAMRepository;
import com.ddr.penerimaandocument.repository.UAM_JoinRepository;
import com.ddr.penerimaandocument.service.MenuListService;
import com.ddr.penerimaandocument.service.UtilService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/menu-list")
public class MenuListController {
    private final Integer CODE_MENU = 6;
     
    @Autowired
    private UAM_JoinRepository uam_JoinRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UAMRepository uamRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private MenuListService menuListService;

    @GetMapping("/")
    public String getMenuList(Model model, HttpServletRequest request) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return "redirect:/";


        List<Uam_Role_Join> data = uam_JoinRepository.findAll();
        List<Role> role = roleRepository.findAll();
        List<UAM> uam = uamRepository.findAll();

        model.addAttribute("menu_role", data);
        model.addAttribute("role", role);
        model.addAttribute("uam", uam);

        return "menu_list/list";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMenuList(HttpServletRequest request, @RequestBody CreateMenuListRequestDTO req) {
        
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        menuListService.addMenuList(req);
        return ResponseEntity.ok("Sukses");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteMenuList(HttpServletRequest request, @RequestBody DeleteMenuListReqDTO req) {
        if(!utilService.compareExactRole((String) request.getSession().getAttribute("token"), CODE_MENU)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        
        menuListService.deleteMenuList(req);
        return ResponseEntity.ok("Sukses");
    }
}
