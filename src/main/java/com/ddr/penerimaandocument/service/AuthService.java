package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddr.penerimaandocument.dto.LoginRequestDTO;
import com.ddr.penerimaandocument.model.User;
import com.ddr.penerimaandocument.repository.UserRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AuthService {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;


    public String doLogin(LoginRequestDTO req){
        System.out.println(req.getUsername());
        User data = userRepository.findByUsername(req.getUsername());
        System.out.println(data);
        if (data == null || !data.getPassword().equals(req.getPassword())) return "kosong";

        System.out.println("masuk");
        String token = jwtService.generateToken(data);
        System.out.println(token);
        return token;
    }

}
