package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddr.penerimaandocument.dto.CreateMenuListRequestDTO;
import com.ddr.penerimaandocument.dto.DeleteMenuListReqDTO;
import com.ddr.penerimaandocument.model.Uam_Role_Join;
import com.ddr.penerimaandocument.repository.RoleRepository;
import com.ddr.penerimaandocument.repository.UAMRepository;
import com.ddr.penerimaandocument.repository.UAM_JoinRepository;

@Service
public class MenuListService {

    @Autowired
    private UAM_JoinRepository uam_JoinRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired
    private UAMRepository uamRepository;
    

    public void addMenuList(CreateMenuListRequestDTO req){
        Uam_Role_Join data = new Uam_Role_Join();

        data.setRole(roleRepository.getReferenceById(req.getRole()));
        data.setUam(uamRepository.getReferenceById(req.getMenu()));

        uam_JoinRepository.save(data);
    }

    public void deleteMenuList(DeleteMenuListReqDTO req){
        for (Integer i : req.getUamJoinId()){
            uam_JoinRepository.deleteById(i);
        }
    }
    
}
