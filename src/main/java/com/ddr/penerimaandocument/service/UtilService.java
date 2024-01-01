package com.ddr.penerimaandocument.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ddr.penerimaandocument.repository.UAM_JoinRepository;
import com.ddr.penerimaandocument.model.Uam_Role_Join;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class UtilService {
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UAM_JoinRepository uam_JoinRepository;


	public boolean compareExactRole(String token, Integer menu){
		Integer role = Integer.parseInt(jwtService.extractRoleFromToken(token));
		List<Uam_Role_Join> permiss = uam_JoinRepository.findExactRole(role, menu);
		return permiss.size() > 0 ? true : false;
	}
}
