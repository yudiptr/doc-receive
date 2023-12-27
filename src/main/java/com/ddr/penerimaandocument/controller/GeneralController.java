package com.ddr.penerimaandocument.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.ddr.penerimaandocument.dto.LoginRequestDTO;
import com.ddr.penerimaandocument.dto.LoginResponseDTO;
import com.ddr.penerimaandocument.service.UtilService;
import org.springframework.beans.factory.ObjectFactory;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class GeneralController {
    
    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
	private UtilService utilService;

    @GetMapping(path = "/")
    public String hello(Model model) throws IOException, InterruptedException{
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

    public void performDatabaseDump() throws IOException, InterruptedException {
        String dbName = "doc-receive";
        String username = "postgres";
        String password = "yudisabri123";
        String dumpPath = "C:\\Users\\YudiSabri\\Desktop\\iniPGDUMP.sql";
        String pgDumpPath = "C:\\Program Files\\PostgreSQL\\14\\bin\\pg_dump.exe";

        ProcessBuilder processBuilder = new ProcessBuilder(
            pgDumpPath, "-p", "5432", "-U", username, "-d", dbName, "-f", dumpPath
        );


        processBuilder.environment().put("PGPASSWORD", password);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        System.out.println(process);
        if (exitCode == 0) {
            System.out.println("Database dump completed successfully!");
        } else {
            System.err.println("Error: Database dump failed!");
        }
    }
    
}
