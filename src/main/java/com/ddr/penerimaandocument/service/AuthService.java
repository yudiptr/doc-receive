package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddr.penerimaandocument.dto.CreateUserDTO;
import com.ddr.penerimaandocument.dto.DeleteUserDTO;
import com.ddr.penerimaandocument.dto.GetUserResDTO;
import com.ddr.penerimaandocument.dto.LoginRequestDTO;
import com.ddr.penerimaandocument.model.User;
import com.ddr.penerimaandocument.repository.RoleRepository;
import com.ddr.penerimaandocument.repository.UserRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    public String doLogin(LoginRequestDTO req){
        User data = userRepository.findByUsername(req.getUsername());
        if (data == null || !data.getPassword().equals(req.getPassword())) return "kosong";
        String token = jwtService.generateToken(data);
        return token;
    }

    public void createUser(CreateUserDTO req){
        User data = new User();
        data.setUsername(req.getUsername());
        data.setPassword(req.getPassword());
        data.setRole(roleRepository.getReferenceById(req.getRole()));

        userRepository.save(data);
    }

    public void deleteUser(DeleteUserDTO req){
        for (Integer i : req.getUserId()){
            userRepository.deleteById(i);
        }
    }

    public GetUserResDTO getUserProfile(Integer userId){
        User data = userRepository.getReferenceById(userId);
        GetUserResDTO res = new GetUserResDTO();
        res.setUser(data);
        return res;
    }

}
